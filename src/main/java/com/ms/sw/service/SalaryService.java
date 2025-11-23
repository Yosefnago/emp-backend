package com.ms.sw.service;

import com.ms.sw.Dto.salaries.SalaryDetailsPerEmployee;
import com.ms.sw.entity.Salary;
import com.ms.sw.repository.EmployeeRepository;
import com.ms.sw.repository.SalaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
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

    public double calculateSalary(String personalId){

        double totalMonthHours = salaryRepository.getTotalHoursMonthByPersonalId(personalId);
        double salaryPerHour = salaryRepository.getSalaryPerHourByPersonalId(personalId);
        double bonus = salaryRepository.getBonusByPersonalId(personalId);

        double totalSalary = totalMonthHours * salaryPerHour + bonus;

        return totalSalary;
    }
    public SalaryDetailsPerEmployee getSalaryByPersonalId(String personalId){
        Salary salary = salaryRepository.getSalaryDetailsByPersonalId(personalId);

        if (salary == null) {
            throw new RuntimeException("לא נמצאו נתוני שכר עבור מספר אישי: " + personalId);
        }

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

        return dto;
    }

}
