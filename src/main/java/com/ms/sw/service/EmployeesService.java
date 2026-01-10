package com.ms.sw.service;

import com.ms.sw.Dto.employee.AddEmployeeRequest;
import com.ms.sw.Dto.employee.EmployeeDetailsResponse;
import com.ms.sw.Dto.employee.EmployeeListResponse;
import com.ms.sw.Dto.employee.UpdateEmployeeDetailsRequest;
import com.ms.sw.entity.ActivityLogs;
import com.ms.sw.entity.Employees;
import com.ms.sw.entity.User;
import com.ms.sw.exception.employees.AddEmployeeException;
import com.ms.sw.exception.employees.EmployeesNotFoundException;
import com.ms.sw.repository.EmployeeRepository;
import com.ms.sw.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EmployeesService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final ActivityLogsService activityLogsService;

    @Autowired
    public EmployeesService(EmployeeRepository employeeRepository,UserRepository userRepository,ActivityLogsService activityLogsService) {
        this.userRepository =  userRepository;
        this.employeeRepository = employeeRepository;
        this.activityLogsService = activityLogsService;
    }

    public List<EmployeeListResponse> getAllEmployees(String username) {

        log.info("EmployeesService::getAllEmployees invoked by user '{}'", username);

        List<Employees> employees = employeeRepository.getAllEmployeesByOwner(username);

        log.info("EmployeesService::getAllEmployees returned {} employees for user '{}'", employees.size(), username);
        if (employees.isEmpty()) {
            return List.of();
        }
        return employees.stream()
                .map(EmployeesService::mapToEmployeeListResponse)
                .toList();
    }
    /**
     * Maps the JPA entity to the list response DTO.
     * @param entity The Employees entity.
     * @return The EmployeeListResponse DTO.
     */
    private static EmployeeListResponse mapToEmployeeListResponse(Employees entity) {
        return new EmployeeListResponse(
                entity.getPersonalId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getDepartment(),
                entity.getStatus()
        );
    }
    @Transactional
    public Employees addEmployee(AddEmployeeRequest addEmployeeRequest, User user) {
        log.info("EmployeesService::addEmployee invoked by user '{}'", user.getUsername());
        try {
            log.info("EmployeesService.addEmployee called with username {}", user.getUsername());
            Employees employee = new Employees();

            employee.setFirstName(addEmployeeRequest.firstName());
            employee.setLastName(addEmployeeRequest.lastName());
            employee.setPersonalId(addEmployeeRequest.personalId());
            employee.setEmail(addEmployeeRequest.email());
            employee.setPhone(addEmployeeRequest.phoneNumber());
            employee.setAddress(addEmployeeRequest.address());
            employee.setPosition(addEmployeeRequest.position());
            employee.setDepartment(addEmployeeRequest.department());
            employee.setHireDate(Timestamp.valueOf(LocalDateTime.now()));
            employee.setStatus(addEmployeeRequest.status());
            employee.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
            employee.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            employee.setUser(user);

            System.out.println(employee.getPhone());
            System.out.println(addEmployeeRequest.phoneNumber());
            log.info("EmployeesService::addEmployee successfully saved employee for user '{}'",
                     user.getUsername());

            ActivityLogs activityLogs = new ActivityLogs();
            activityLogs.setFromUser(user.getUsername());
            activityLogs.setAction("add new employee %s %s".formatted(employee.getFirstName(), employee.getLastName()));
            activityLogs.setDateAction(LocalDate.now());
            activityLogs.setTimeAction(LocalTime.now());

            activityLogsService.save(activityLogs);

            return employeeRepository.save(employee);


        } catch (DataIntegrityViolationException e) {
            log.error("EmployeesService::addEmployee failed due to data integrity for user '{}': {}",
                    user.getUsername(), e.getMessage());
            throw new AddEmployeeException("Error: Employee data violates unique constraints (e.g., ID or Email already exists).");
        }

    }
    public EmployeeDetailsResponse getEmployeeByPersonalId(String personalId, String username) {
        log.info("EmployeesService::getEmployeeByPersonalId invoked by user '{}' for personalId '{}'", username, personalId);


        Employees employee = employeeRepository.findByPersonalIdAndOwner(personalId, username)
                .orElseThrow(() -> new EmployeesNotFoundException("Employee not found or unauthorized."));

        return mapToEmployeeDetailsResponse(employee);
    }
    private EmployeeDetailsResponse mapToEmployeeDetailsResponse(Employees entity) {

        return new EmployeeDetailsResponse(
                entity.getFirstName(),
                entity.getLastName(),
                entity.getPersonalId(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getPosition(),
                entity.getDepartment(),
                entity.getAddress(),
                entity.getHireDate(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
    @Transactional
    public void deleteEmployee(String id, String username) {
        log.info("EmployeesService::deleteEmployee invoked by user '{}' for personalId '{}'", username, id);
        Optional<Employees> employee = employeeRepository.findByPersonalId(id);

        if (employee.isEmpty()) {
            throw new EmployeesNotFoundException("Employee not found or unauthorized to delete.");
        }

        ActivityLogs activityLogs = new ActivityLogs();
        activityLogs.setFromUser(username);
        activityLogs.setAction("delete employee %s %s".formatted(
                employee.get().getFirstName(),
                employee.get().getLastName()
        ));
        activityLogs.setDateAction(LocalDate.now());
        activityLogs.setTimeAction(LocalTime.now());
        activityLogsService.save(activityLogs);

        int deletedCount = employeeRepository.deleteByPersonalIdAndOwner(id, username);

        if (deletedCount == 0) {
            throw new EmployeesNotFoundException("Employee not found or unauthorized to delete.");
        }
    }

    @Transactional
    public void updateEmployeeDetails(UpdateEmployeeDetailsRequest updateEmployeeDetailsRequest, String username) {

        log.info("EmployeesService::updateEmployeeDetails invoked by user '{}' for personalId '{}'",
                username, updateEmployeeDetailsRequest.personal_id());

        int updatedCount = employeeRepository.updateEmployeeDetailsByPersonalIdAndOwner(
                updateEmployeeDetailsRequest.personal_id(),
                username,
                updateEmployeeDetailsRequest.firstName(),
                updateEmployeeDetailsRequest.lastName(),
                updateEmployeeDetailsRequest.email(),
                updateEmployeeDetailsRequest.position(),
                updateEmployeeDetailsRequest.department(),
                updateEmployeeDetailsRequest.status()
        );

        if (updatedCount == 0) {
            log.warn("EmployeesService::updateEmployeeDetails failed: Employee not found or unauthorized for user '{}', ID '{}'",
                    username, updateEmployeeDetailsRequest.personal_id());
            throw new EmployeesNotFoundException("Employee not found or unauthorized to update.");
        }

        log.info("EmployeesService::updateEmployeeDetails successfully updated personalId '{}' for user '{}'",
                updateEmployeeDetailsRequest.personal_id(), username);

    }
}
