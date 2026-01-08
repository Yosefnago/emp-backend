package com.ms.sw.service;

import com.ms.sw.Dto.DashboardStatsResponse;
import com.ms.sw.repository.EmployeeRepository;
import com.ms.sw.repository.SalaryRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    EmployeeRepository employeeRepository;
    SalaryRepository salaryRepository;


    public DashboardService(EmployeeRepository employeeRepository, SalaryRepository salaryRepository) {
        this.employeeRepository = employeeRepository;
        this.salaryRepository = salaryRepository;
    }
    public DashboardStatsResponse loadDashboardData(String username){

        DashboardStatsResponse response = new DashboardStatsResponse(
                employeeRepository.countEmployeesByUsername(username),
                54,
                salaryRepository.getSalariesByOwner(username),
                4

        );
        return response;
    }

}
