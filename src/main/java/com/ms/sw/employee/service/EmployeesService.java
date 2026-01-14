package com.ms.sw.employee.service;

import com.ms.sw.employee.dto.AddEmployeeRequest;
import com.ms.sw.employee.dto.EmployeeDetailsResponse;
import com.ms.sw.employee.dto.EmployeeListResponse;
import com.ms.sw.employee.dto.UpdateEmployeeDetailsRequest;
import com.ms.sw.employee.model.Employees;
import com.ms.sw.user.model.User;
import com.ms.sw.exception.employees.AddEmployeeException;
import com.ms.sw.exception.employees.EmployeesNotFoundException;
import com.ms.sw.employee.repo.EmployeeRepository;
import com.ms.sw.user.repo.UserRepository;
import com.ms.sw.views.service.ActivityLogsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
            employee.setCity(addEmployeeRequest.city());
            employee.setCountry(addEmployeeRequest.country());
            employee.setPosition(addEmployeeRequest.position());
            employee.setDepartment(addEmployeeRequest.department());
            employee.setHireDate(addEmployeeRequest.hireDate());
            employee.setStatus(addEmployeeRequest.status());
            employee.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            employee.setUser(user);

            System.out.println(employee.getPhone());
            System.out.println(addEmployeeRequest.phoneNumber());
            log.info("EmployeesService::addEmployee successfully saved employee for user '{}'",
                     user.getUsername());

            activityLogsService.logAction("employee %s %s".formatted(employee.getFirstName(), employee.getLastName()), user.getUsername());

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
                entity.getGender(),
                entity.getBirthDate(),
                entity.getFamilyStatus(),
                entity.getPhone(),
                entity.getPosition(),
                entity.getDepartment(),
                entity.getAddress(),
                entity.getCity(),
                entity.getCountry(),
                entity.getHireDate(),
                entity.getJobType(),
                entity.getStatus(),
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

        activityLogsService.logAction("added new employee %s %s".formatted(
                employee.get().getFirstName(), employee.get().getLastName()
        ), username);

        int deletedCount = employeeRepository.deleteByPersonalIdAndOwner(id, username);

        if (deletedCount == 0) {
            throw new EmployeesNotFoundException("Employee not found or unauthorized to delete.");
        }
    }

    @Transactional
    public void updateEmployeeDetails(UpdateEmployeeDetailsRequest request, String username, String personalId) {

        log.info("EmployeesService::updateEmployeeDetails invoked by user '{}' for personalId '{}'",
                username, personalId);

        int updatedCount = employeeRepository.updateEmployeeDetailsByPersonalIdAndOwner(
                personalId,
                username,
                request.firstName(),
                request.lastName(),
                request.email(),
                request.gender(),
                request.birthDate(),
                request.familyStatus(),
                request.phone(),
                request.address(),
                request.city(),
                request.country(),
                request.position(),
                request.department(),
                request.hireDate(),
                request.jobType(),
                request.status()
        );
        activityLogsService.logAction("update employee %s %s".formatted(
                request.firstName(),request.lastName()
        ), username);


        if (updatedCount == 0) {
            log.warn("EmployeesService::updateEmployeeDetails failed: Employee not found or unauthorized for user '{}', ID '{}'",
                    username, personalId);
            throw new EmployeesNotFoundException("Employee not found or unauthorized to update.");
        }

        log.info("EmployeesService::updateEmployeeDetails successfully updated personalId '{}' for user '{}'",
                personalId, username);

    }
}
