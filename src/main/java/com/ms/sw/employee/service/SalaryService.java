package com.ms.sw.employee.service;

import com.ms.sw.attendance.dto.AttendancePayrollDto;
import com.ms.sw.attendance.dto.AttendanceSummaryRequest;
import com.ms.sw.attendance.service.AttendanceService;
import com.ms.sw.employee.dto.*;
import com.ms.sw.employee.model.Employees;
import com.ms.sw.employee.model.Salary;
import com.ms.sw.employee.repo.SalaryDetailsRepository;
import com.ms.sw.employee.repo.SalaryRepository;
import com.ms.sw.exception.employee.EmployeeNotFoundException;
import com.ms.sw.user.model.ActionType;
import com.ms.sw.user.model.User;
import com.ms.sw.user.service.ActivityLogsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.*;

import static com.ms.sw.employee.service.PayrollConstants.*;

/**
 * Service responsible for calculating employee salaries.
 *
 * @author Yosef Nago
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SalaryService {

    private final EmployeesService employeesService;
    private final AttendanceService attendanceService;
    private final SalaryDetailsRepository salaryDetailsRepository;
    private final SalaryRepository salaryRepository;
    private final PdfGeneratorService pdfGenerator;
    private final ActivityLogsService activityLogsService;

    /**
     * Fetches salary data and calculates payroll.
     */
    public void fetchSalaryData(User user, AttendanceSummaryRequest request) {

        // changed from CompletableFuture to StructuredTaskScope
        try(var scope = StructuredTaskScope.open(StructuredTaskScope.Joiner.awaitAllSuccessfulOrThrow())){

            var employeeData = scope.fork(()->employeesService.getEmployeePayrollByPersonalId(user.getUsername(),request.personalId()));
            var attendanceData = scope.fork(()-> attendanceService.getAttendancePayrollByPersonalId(user.getUsername(), request));
            var salaryData = scope.fork(()-> salaryDetailsRepository.findSalaryDetailsOfEmployee(user.getUsername(),request.personalId()));

            scope.join();

            var emp = employeeData.get();
            var atd = attendanceData.get();
            var sal = salaryData.get();

            validateFetchedData(emp,atd,sal);
            calculateSalary(user, emp, atd, sal);

            activityLogsService.logAction(ActionType.SENT_TO_PAYROLL,"לעובד "+request.employeeName(),user.getUsername());
        }catch (StructuredTaskScope.FailedException | InterruptedException e){
            log.debug("Error while trying to fetch data from db");
            log.error("{}",e.getMessage());
        }
    }

    /**
     * Main salary calculation.
     */
    public void calculateSalary(User user, EmployeePayrollDto employee,
                                List<AttendancePayrollDto> attendance,
                                SalaryDetailsDto salaryDetails) {

        OvertimeResult overtimeHours = calculateOvertimeHours(attendance);
        long travelDays = calculateTravelDays(attendance);

        GrossSalaryResult grossSalary = calculateGrossSalary(
                overtimeHours,
                travelDays,
                salaryDetails.salaryPerHour()
        );

        DeductionsResult deductions = calculateDeductions(
                grossSalary.total(),
                salaryDetails.creditPoints()
        );

        double netSalary = grossSalary.total() - deductions.total();
        double employerCost = calculateEmployerCost(grossSalary.total(), deductions);

        SalarySlipData pdfData = buildSalarySlipData(
                user, employee, attendance, salaryDetails,
                overtimeHours, travelDays, grossSalary, deductions, netSalary, employerCost
        );

        String pdfPath = pdfGenerator.generateSalaryPdf(pdfData);
        saveSalaryRecord(user.getUsername(), employee.personalId(), pdfData, netSalary, pdfPath);

        // log to be removed later. by adding time schedular for generating tlush.
        activityLogsService.logAction(ActionType.GENERATED_PAYROLL,"לעובד "+employee.employeeName(),user.getUsername());
        log.info("Salary generated for {} ({})", employee.employeeName(), employee.personalId());
    }

    private void validateFetchedData(EmployeePayrollDto employee,
                                     List<AttendancePayrollDto> attendance,
                                     SalaryDetailsDto salaryDetails) {
        if (employee == null) {
            throw new EmployeeNotFoundException("Employee not found");
        }
        if (attendance == null || attendance.isEmpty()) {
            throw new IllegalStateException("Attendance not found");
        }
        if (salaryDetails == null) {
            throw new IllegalStateException("Salary details not found");
        }
        if (!employee.personalId().equals(attendance.getFirst().personalId())) {
            throw new EmployeeNotFoundException("Mismatch between Employee and Attendance data");
        }
    }

    /**
     * GROSS SALARY CALCULATION
     */
    private record GrossSalaryResult(
            double regularPay,
            double overtime125Pay,
            double overtime150Pay,
            double travelAllowance,
            double total
    ) {}

    private GrossSalaryResult calculateGrossSalary(OvertimeResult hours,
                                                   long travelDays,
                                                   double hourlyRate) {
        double regularPay = hours.regularHours() * hourlyRate;
        double overtime125Pay = hours.hours125() * hourlyRate * RATE_125;
        double overtime150Pay = hours.hours150() * hourlyRate * RATE_150;
        double travelAllowance = travelDays * DAILY_TRAVEL_RATE;

        double total = roundToAgorot(regularPay + overtime125Pay + overtime150Pay + travelAllowance);

        return new GrossSalaryResult(
                regularPay, overtime125Pay, overtime150Pay, travelAllowance, total
        );
    }

    private long calculateTravelDays(List<AttendancePayrollDto> attendance) {
        return attendance.stream()
                .filter(AttendancePayrollDto::travelAllow)
                .count();
    }

    /**
     * DEDUCTIONS CALCULATION
     */
    private record DeductionsResult(
            double employeePension,
            double nationalInsurance,
            double incomeTax,
            double total,
            SocialContributionsResult socialContributions,
            NationalInsuranceResult niResult,
            IncomeTaxResult taxResult
    ) {}

    private DeductionsResult calculateDeductions(double grossSalary, double creditPoints) {
        SocialContributionsResult social = calculatePensionContributions(grossSalary);
        NationalInsuranceResult ni = calculateNationalInsurance(grossSalary);

        double taxableIncome = grossSalary - social.employeePension();
        IncomeTaxResult tax = calculateIncomeTax(taxableIncome, creditPoints);

        double total = social.employeePension() + ni.employeeDeduction() + tax.netTax();

        return new DeductionsResult(
                social.employeePension(),
                ni.employeeDeduction(),
                tax.netTax(),
                total,
                social,
                ni,
                tax
        );
    }

    private double calculateEmployerCost(double grossSalary, DeductionsResult deductions) {
        return grossSalary +
                deductions.socialContributions().totalEmployerSocials() +
                deductions.niResult().employerContribution();
    }

    /**
     * OVERTIME CALCULATION.
     */
    public record OvertimeResult(double regularHours, double hours125, double hours150) {}

    private OvertimeResult calculateOvertimeHours(List<AttendancePayrollDto> attendance) {
        double regularHours = 0;
        double hours125 = 0;
        double hours150 = 0;

        for (AttendancePayrollDto day : attendance) {
            double dailyHours = day.totalHours();

            if (dailyHours <= REGULAR_DAILY_HOURS_LIMIT) {
                regularHours += dailyHours;
            } else {
                regularHours += REGULAR_DAILY_HOURS_LIMIT;
                double extra = dailyHours - REGULAR_DAILY_HOURS_LIMIT;

                if (extra <= OVERTIME_125_LIMIT) {
                    hours125 += extra;
                } else {
                    hours125 += OVERTIME_125_LIMIT;
                    hours150 += (extra - OVERTIME_125_LIMIT);
                }
            }
        }
        return new OvertimeResult(regularHours, hours125, hours150);
    }

    /**
     * PENSION & SEVERANCE
     */
    public record SocialContributionsResult(
            double employeePension,
            double employerPension,
            double employerSeverance,
            double totalEmployerSocials
    ) {}

    private SocialContributionsResult calculatePensionContributions(double pensionableSalary) {
        double employeePension = pensionableSalary * PENSION_EMPLOYEE_RATE;
        double employerPension = pensionableSalary * PENSION_EMPLOYER_RATE;
        double employerSeverance = pensionableSalary * SEVERANCE_EMPLOYER_RATE;
        double totalEmployerSocials = employerPension + employerSeverance;

        return new SocialContributionsResult(
                employeePension, employerPension, employerSeverance, totalEmployerSocials
        );
    }

    /**
     * NATIONAL INSURANCE
     */
    public record NationalInsuranceResult(double employeeDeduction, double employerContribution) {}

    private NationalInsuranceResult calculateNationalInsurance(double grossSalary) {
        double employeeDeduction = calculateEmployeeNI(grossSalary);
        double employerContribution = calculateEmployerNI(grossSalary);
        return new NationalInsuranceResult(employeeDeduction, employerContribution);
    }

    private double calculateEmployeeNI(double gross) {
        if (gross <= NI_INCOME_THRESHOLD) {
            return gross * NI_EMPLOYEE_REDUCED_RATE;
        }
        double firstBracket = NI_INCOME_THRESHOLD * NI_EMPLOYEE_REDUCED_RATE;
        double secondBracket = (gross - NI_INCOME_THRESHOLD) * NI_EMPLOYEE_FULL_RATE;
        return firstBracket + secondBracket;
    }

    private double calculateEmployerNI(double gross) {
        if (gross <= NI_INCOME_THRESHOLD) {
            return gross * NI_EMPLOYER_REDUCED_RATE;
        }
        double firstBracket = NI_INCOME_THRESHOLD * NI_EMPLOYER_REDUCED_RATE;
        double secondBracket = (gross - NI_INCOME_THRESHOLD) * NI_EMPLOYER_FULL_RATE;
        return firstBracket + secondBracket;
    }

    /**
     * INCOME TAX
     */
    public record IncomeTaxResult(double grossTax, double creditPointsValue, double netTax) {}

    private IncomeTaxResult calculateIncomeTax(double taxableSalary, double creditPoints) {
        double grossTax = calculateTaxBrackets(taxableSalary);
        double totalCreditDiscount = creditPoints * CREDIT_POINT_VALUE;
        double netTax = Math.max(0, grossTax - totalCreditDiscount);

        return new IncomeTaxResult(grossTax, totalCreditDiscount, netTax);
    }

    private double calculateTaxBrackets(double income) {
        double tax = 0;

        if (income > 0) tax += Math.min(income, TAX_BRACKET_1_LIMIT) * TAX_BRACKET_1_RATE;
        if (income > TAX_BRACKET_1_LIMIT) tax += (Math.min(income, TAX_BRACKET_2_LIMIT) - TAX_BRACKET_1_LIMIT) * TAX_BRACKET_2_RATE;
        if (income > TAX_BRACKET_2_LIMIT) tax += (Math.min(income, TAX_BRACKET_3_LIMIT) - TAX_BRACKET_2_LIMIT) * TAX_BRACKET_3_RATE;
        if (income > TAX_BRACKET_3_LIMIT) tax += (Math.min(income, TAX_BRACKET_4_LIMIT) - TAX_BRACKET_3_LIMIT) * TAX_BRACKET_4_RATE;
        if (income > TAX_BRACKET_4_LIMIT) tax += (Math.min(income, TAX_BRACKET_5_LIMIT) - TAX_BRACKET_4_LIMIT) * TAX_BRACKET_5_RATE;
        if (income > TAX_BRACKET_5_LIMIT) tax += (Math.min(income, TAX_BRACKET_6_LIMIT) - TAX_BRACKET_5_LIMIT) * TAX_BRACKET_6_RATE;
        if (income > TAX_BRACKET_6_LIMIT) tax += (income - TAX_BRACKET_6_LIMIT) * TAX_BRACKET_7_RATE;

        return tax;
    }

    /**
     * DATA BUILDING
     */
    private SalarySlipData buildSalarySlipData(
            User user, EmployeePayrollDto employee, List<AttendancePayrollDto> attendance,
            SalaryDetailsDto salaryDetails, OvertimeResult hours, long travelDays,
            GrossSalaryResult gross, DeductionsResult deductions,
            double netSalary, double employerCost) {

        LocalDate firstAttendanceDate = attendance.getFirst().date();
        double taxableIncome = gross.total() - deductions.employeePension();

        return new SalarySlipData(
                user.getCompanyName(), user.getCompanyId(), user.getCompanyAddress(),
                firstAttendanceDate.getYear(), firstAttendanceDate.getMonthValue(),
                employee.employeeName(), employee.personalId(), employee.department(),
                LocalDate.now(),
                salaryDetails.pensionFund(),salaryDetails.providentFund(),salaryDetails.insuranceCompany(),
                hours.regularHours(), hours.hours125(), hours.hours150(), travelDays,
                salaryDetails.salaryPerHour(),
                gross.regularPay(), gross.overtime125Pay(), gross.overtime150Pay(),
                gross.travelAllowance(), gross.total(),
                deductions.employeePension(), deductions.nationalInsurance(),
                taxableIncome, deductions.incomeTax(), deductions.total(), netSalary,
                salaryDetails.creditPoints(),
                deductions.socialContributions().employerPension(),
                deductions.socialContributions().employerSeverance(),
                deductions.niResult().employerContribution(),
                employerCost
        );
    }

    private void saveSalaryRecord(String username, String personalId,
                                  SalarySlipData pdfData, double netSalary, String pdfPath) {
        Employees employee = employeesService.getEmployeeEntityByPersonalId(personalId, username);

        Salary salary = new Salary();
        salary.setEmployee(employee);
        salary.setSalaryAmount(netSalary);
        salary.setSalaryMonth(pdfData.month());
        salary.setSalaryYear(pdfData.year());
        salary.setPaymentDate(LocalDate.now());
        salary.setPathOfTlush(pdfPath);

        salaryRepository.save(salary);
    }

    /**
     * UTILITY
     */
    private double roundToAgorot(double amount) {
        return BigDecimal.valueOf(amount)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}