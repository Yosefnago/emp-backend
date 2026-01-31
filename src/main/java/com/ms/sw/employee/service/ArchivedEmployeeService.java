package com.ms.sw.employee.service;

import com.ms.sw.employee.model.ArchivedEmployees;
import com.ms.sw.employee.model.Employees;
import com.ms.sw.employee.repo.ArchivedEmployeeRepository;
import com.ms.sw.employee.repo.EmployeeRepository;
import com.ms.sw.exception.employees.EmployeeNotFoundException;
import com.ms.sw.user.model.ActionType;
import com.ms.sw.user.service.ActivityLogsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Service responsible for archiving (soft deleting) employees.
 *
 * <p>Moves an employee from the active employee table to the archived employees table,
 * records who performed the action, and logs the activity.</p>
 *
 * <p>Transactional: ensures that archiving and deletion occur atomically.</p>
 */
@Service
public class ArchivedEmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ActivityLogsService activityLogsService;
    private final ArchivedEmployeeRepository archivedEmployeeRepository;

    public  ArchivedEmployeeService(EmployeeRepository employeeRepository, ActivityLogsService activityLogsService, ArchivedEmployeeRepository archivedEmployeeRepository) {
        this.employeeRepository = employeeRepository;
        this.activityLogsService = activityLogsService;
        this.archivedEmployeeRepository = archivedEmployeeRepository;
    }

    /**
     * Archives an employee by moving their record to the archived table and removing from active employees.
     *
     * <p>Performs the following steps:</p>
     * <ol>
     *     <li>Validates that the employee exists and belongs to the requesting user</li>
     *     <li>Copies employee details to {@link ArchivedEmployees}</li>
     *     <li>Saves archived record with archiving metadata</li>
     *     <li>Logs the archive action using {@link ActivityLogsService}</li>
     *     <li>Deletes the employee from the active employees table</li>
     * </ol>
     *
     * @param personalId the personal ID of the employee to archive
     * @param username the username of the user performing the archive action
     * @throws EmployeeNotFoundException if the employee does not exist or does not belong to the user
     * @throws RuntimeException if archiving fails due to database or other unexpected errors
     */
    @Transactional
    public void archiveEmployee(String personalId, String username) {
        Optional<Employees> employeeOpt = employeeRepository.findByPersonalId(personalId);

        if (employeeOpt.isEmpty()) {
            throw new EmployeeNotFoundException("Employee not found or unauthorized to archive.");
        }

        Employees employee = employeeOpt.get();

        if (!employee.getUser().getUsername().equals(username)) {
            throw new EmployeeNotFoundException("Employee not found or unauthorized to archive.");
        }

        try {
            ArchivedEmployees archivedEmployee = new ArchivedEmployees();

            archivedEmployee.setEmployeeId(employee.getId());
            archivedEmployee.setFirstName(employee.getFirstName());
            archivedEmployee.setLastName(employee.getLastName());
            archivedEmployee.setPersonalId(employee.getPersonalId());
            archivedEmployee.setGender(employee.getGender());
            archivedEmployee.setBirthDate(employee.getBirthDate());
            archivedEmployee.setFamilyStatus(employee.getFamilyStatus());
            archivedEmployee.setEmail(employee.getEmail());
            archivedEmployee.setPhone(employee.getPhoneNumber());
            archivedEmployee.setAddress(employee.getAddress());
            archivedEmployee.setCity(employee.getCity());
            archivedEmployee.setCountry(employee.getCountry());
            archivedEmployee.setPosition(employee.getPosition());
            archivedEmployee.setDepartment(employee.getDepartment());
            archivedEmployee.setHireDate(employee.getHireDate());
            archivedEmployee.setJobType(employee.getJobType());
            archivedEmployee.setStatus(employee.getStatus());
            archivedEmployee.setUpdatedAt(employee.getUpdatedAt());

            archivedEmployee.setArchivedAt(LocalDate.now());
            archivedEmployee.setArchivedBy(username);

            archivedEmployeeRepository.save(archivedEmployee);

            activityLogsService.logAction(ActionType.ARCHIVE,employee.getFirstName().concat(" "+employee.getLastName()),username);

            int deletedCount = employeeRepository.deleteByPersonalIdAndOwner(personalId, username);

            if (deletedCount == 0) {
                throw new EmployeeNotFoundException("Failed to delete employee from active table.");
            }


        } catch (Exception e) {
            throw new RuntimeException("Failed to archive employee: " + e.getMessage(), e);
        }
    }

}
