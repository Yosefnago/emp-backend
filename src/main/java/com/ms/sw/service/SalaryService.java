package com.ms.sw.service;

import com.ms.sw.repository.EmployeeRepository;
import com.ms.sw.repository.SalaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

}
