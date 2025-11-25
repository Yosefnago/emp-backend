package com.ms.sw.service;

import com.ms.sw.Dto.salaries.SalaryCalculationProjection;
import com.ms.sw.Dto.salaries.SalaryDetailsPerEmployee;
import com.ms.sw.entity.Salary;
import com.ms.sw.exception.salary.SalaryNotFoundException;
import com.ms.sw.repository.EmployeeRepository;
import com.ms.sw.repository.SalaryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class SalaryService {

    private final SalaryRepository salaryRepository;
    private final EmployeeRepository employeeRepository;


    @Autowired
    public SalaryService(SalaryRepository salaryRepository, EmployeeRepository employeeRepository) {
        this.salaryRepository = salaryRepository;
        this.employeeRepository = employeeRepository;
    }


    public BigDecimal getAllSalaryOfAllEmployees(String username){
        return salaryRepository.getSalariesByOwner(username);
    }

    public double calculateSalary(String personalId, String username){

        log.info("SalaryService::calculateSalary invoked by user '{}' for employee '{}'", username, personalId);

        SalaryCalculationProjection data = salaryRepository.getSalaryCalculationDataByPersonalIdAndOwner(personalId, username)
                .orElseThrow(() -> {
                    log.warn("SalaryService::calculateSalary failed: No salary data found for ID '{}', user '{}'", personalId, username);
                    return new SalaryNotFoundException("Salary data not found or unauthorized for employee: " + personalId);
                });

        double totalMonthHours = data.getTotalHoursMonth();
        double salaryPerHour = data.getSalaryPerHour();
        double bonus = data.getBonus();

        double totalSalary = totalMonthHours * salaryPerHour + bonus;

        log.info("SalaryService::calculateSalary success for employee '{}', total: {}", personalId, totalSalary);
        return totalSalary;
    }
    public SalaryDetailsPerEmployee getSalaryByPersonalId(String personalId, String username){ // <- Added username

        log.info("SalaryService::getSalaryByPersonalId invoked by user '{}' for employee '{}'", username, personalId);

        // Fetch salary details, checking ownership (security).
        Salary salary = salaryRepository.getSalaryDetailsByPersonalIdAndOwner(personalId, username)
                .orElseThrow(() -> {
                    log.warn("SalaryService::getSalaryByPersonalId failed: No details found for ID '{}', user '{}'", personalId, username);
                    return new SalaryNotFoundException("Salary details not found or unauthorized for employee: " + personalId);
                });

        // Mapping logic remains the same
        SalaryDetailsPerEmployee dto = new SalaryDetailsPerEmployee(
                salary.getSalaryPerHour(),
                salary.getMonthlySalary(),
                salary.getBonus(),
                salary.getBankName(),
                salary.getBankAccount(),
                salary.getPensionFund(),
                salary.getTotalHoursMonth(),
                salary.getOvertimeHours(),
                salary.getVacationDays()
        );

        log.info("SalaryService::getSalaryByPersonalId success for employee '{}'", personalId);
        return dto;
    }

}
