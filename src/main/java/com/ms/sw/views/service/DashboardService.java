package com.ms.sw.views.service;

import com.ms.sw.views.dto.DashboardStatsResponse;
import com.ms.sw.employee.repo.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    EmployeeRepository employeeRepository;


    public DashboardService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    public DashboardStatsResponse loadDashboardData(String username){

        DashboardStatsResponse response = new DashboardStatsResponse(
                employeeRepository.countEmployeesByUsername(username),
                employeeRepository.countEmployeeByStatus(username),
                4

        );
        return response;
    }

}
