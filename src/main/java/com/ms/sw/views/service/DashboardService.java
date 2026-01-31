package com.ms.sw.views.service;

import com.ms.sw.views.dto.DashboardStatsResponse;
import com.ms.sw.employee.repo.EmployeeRepository;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for loading and aggregating dashboard data
 * related to employees for a given user.
 */
@Service
public class DashboardService {

    EmployeeRepository employeeRepository;


    public DashboardService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Loads the dashboard statistics for the specified user.
     *
     * <p>Statistics include:</p>
     * <ul>
     *     <li>Total number of employees owned by the user</li>
     *     <li>Number of employees currently marked as present</li>
     *     <li>Number of projects onboard (currently hardcoded as 4)</li>
     * </ul>
     *
     * @param username the username of the authenticated user
     * @return {@link DashboardStatsResponse} containing the aggregated statistics
     */
    public DashboardStatsResponse loadDashboardData(String username){

        int countEmployee = employeeRepository.countEmployeesByUsername(username);
        int statusCount = employeeRepository.countEmployeeByStatus(username);

        return new DashboardStatsResponse(countEmployee, statusCount, 4);
    }

}
