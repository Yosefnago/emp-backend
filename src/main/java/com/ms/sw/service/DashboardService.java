package com.ms.sw.service;

import com.ms.sw.Dto.DashboardStatsResponse;
import com.ms.sw.repository.EmployeeRepository;
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
                3,
                4

        );
        return response;
    }

}
