package com.ms.sw.employee.service;

import com.ms.sw.employee.dto.*;
import com.ms.sw.employee.model.Employees;
import com.ms.sw.exception.employees.EmployeeNotFoundException;
import com.ms.sw.user.model.ActionType;
import com.ms.sw.user.model.User;
import com.ms.sw.exception.employees.AddEmployeeException;
import com.ms.sw.employee.repo.EmployeeRepository;
import com.ms.sw.user.service.ActivityLogsService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

/**
 * Service for managing active employees.
 *
 * <p>Provides methods to add, retrieve, and update employee records.
 * Each operation also logs the corresponding activity for audit purposes.</p>
 */
@Service
public class EmployeesService {

    private final EmployeeRepository employeeRepository;
    private final ActivityLogsService activityLogsService;

    public EmployeesService(EmployeeRepository employeeRepository,ActivityLogsService activityLogsService) {
        this.employeeRepository = employeeRepository;
        this.activityLogsService = activityLogsService;
    }

    /**
     * Retrieves all employees owned by a specific user.
     *
     * @param username the username of the owner
     * @return list of {@link EmployeeListResponse}, empty if none exist
     */
    public List<EmployeeListResponse> getAllEmployees(String username) {

        List<EmployeeListResponse> employees = employeeRepository.getAllEmployeesByOwner(username);

        if (employees.isEmpty()) {
            return List.of();
        }
        return employees;
    }

    /**
     * Adds a new employee and logs the action.
     *
     * @param addEmployeeRequest DTO containing employee details
     * @param user the user performing the addition
     * @throws AddEmployeeException if the employee violates unique constraints (e.g., ID or email)
     */
    @Transactional
    public void addEmployee(AddEmployeeRequest addEmployeeRequest, User user) {

        try {
            Employees employee = new Employees();

            employee.setFirstName(addEmployeeRequest.firstName());
            employee.setLastName(addEmployeeRequest.lastName());
            employee.setPersonalId(addEmployeeRequest.personalId());
            employee.setEmail(addEmployeeRequest.email());
            employee.setPhoneNumber(addEmployeeRequest.phoneNumber());
            employee.setAddress(addEmployeeRequest.address());
            employee.setCity(addEmployeeRequest.city());
            employee.setCountry(addEmployeeRequest.country());
            employee.setPosition(addEmployeeRequest.position());
            employee.setDepartment(addEmployeeRequest.department());
            employee.setHireDate(addEmployeeRequest.hireDate());
            employee.setStatus(addEmployeeRequest.status());
            employee.setUpdatedAt(LocalDate.now());
            employee.setUser(user);

            employeeRepository.save(employee);

            activityLogsService.logAction(
                    ActionType.ADD,
                    employee.getFirstName().concat(" "+employee.getLastName())
                    ,user.getUsername());

        } catch (DataIntegrityViolationException e) {

            throw new AddEmployeeException("Error: Employee data violates unique constraints (e.g., ID or Email already exists).");
        }
    }

    /**
     * Retrieves an employee by personal ID and owner.
     *
     * @param personalId the personal ID of the employee
     * @param username the username of the owner
     * @return {@link EmployeeDetailsResponse} with employee details
     * @throws EmployeeNotFoundException if the employee does not exist or is unauthorized
     */
    public EmployeeDetailsResponse getEmployeeByPersonalId(String personalId, String username) {


        return employeeRepository.findByPersonalIdAndOwner(personalId, username)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found or unauthorized."));
    }

    /**
     * Updates details of an existing employee and logs the action.
     *
     * @param request DTO containing updated employee fields
     * @param username the username of the owner performing the update
     * @param personalId the personal ID of the employee to update
     * @throws EmployeeNotFoundException if the employee does not exist or is unauthorized
     */
    @Transactional
    public void updateEmployeeDetails(UpdateEmployeeDetailsRequest request, String username, String personalId) {

        int updatedCount = employeeRepository.updateEmployeeDetailsByPersonalIdAndOwner(
                personalId,
                username,
                request.firstName(),
                request.lastName(),
                request.email(),
                request.gender(),
                request.birthDate(),
                request.familyStatus(),
                request.phoneNumber(),
                request.address(),
                request.city(),
                request.country(),
                request.position(),
                request.department(),
                request.hireDate(),
                request.jobType(),
                request.status()
        );

        activityLogsService.logAction(ActionType.UPDATE,request.firstName().concat(" "+request.lastName()),username);

        if (updatedCount == 0) {
            throw new EmployeeNotFoundException("Employee not found or unauthorized to update.");
        }
    }

    public EmployeePayrollDto getEmployeePayrollByPersonalId(String username, String personalId) {
        return employeeRepository.getEmployeePayrollByPersonalId(username, personalId);
    }
    public Employees getEmployeeEntityByPersonalId(String personalId, String username) {
        return employeeRepository.findEntityByPersonalIdAndUsername(personalId, username)
                .orElseThrow(() -> new EmployeeNotFoundException(
                        "Employee not found: " + personalId));
    }
}
