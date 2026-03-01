package com.ms.sw.employee.service;

import com.ms.sw.attendance.repo.AttendanceRepository;
import com.ms.sw.employee.model.Employees;
import com.ms.sw.employee.repo.EmployeeRepository;
import com.ms.sw.exception.employee.EmployeeNotFoundException;
import com.ms.sw.user.model.ActionType;
import com.ms.sw.user.service.ActivityLogsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

/**
 * Service responsible for archiving (soft deleting) employees.
 *
 * <p>Moves an employee from the active employee table to the archived employees table,
 * records who performed the action, and logs the activity.</p>
 *
 * <p>Transactional: ensures that archiving and deletion occur atomically.</p>
 */
@Service
@Slf4j
public class ArchivedEmployeeService {

    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final ActivityLogsService activityLogsService;

    public  ArchivedEmployeeService(EmployeeRepository employeeRepository,
                                    ActivityLogsService activityLogsService,
                                    AttendanceRepository attendanceRepository) {
        this.employeeRepository = employeeRepository;
        this.attendanceRepository = attendanceRepository;
        this.activityLogsService = activityLogsService;
    }

    @Transactional
    public void archiveEmployee(String personalId, String username) {
        log.info("Archiving employee: {} by changing status to INACTIVE", personalId);

        Employees employee = employeeRepository.findEntityByPersonalIdAndUsername(personalId, username)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

        employee.setStatus("INACTIVE");
        employee.setUpdatedAt(LocalDate.now());
        employeeRepository.save(employee);

        attendanceRepository.deleteByEmployeeId(employee.getId());
        activityLogsService.logAction(ActionType.ARCHIVE,employee.getFirstName()+" "+employee.getLastName(),username);
        log.info("Employee {} is now INACTIVE. Attendance cleared, salaries preserved.", personalId);
    }

}
