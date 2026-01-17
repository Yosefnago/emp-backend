package com.ms.sw.employee.service;

import com.ms.sw.employee.dto.AddEmployeeRequest;
import com.ms.sw.employee.dto.EmployeeDetailsResponse;
import com.ms.sw.employee.dto.EmployeeListResponse;
import com.ms.sw.employee.dto.UpdateEmployeeDetailsRequest;
import com.ms.sw.employee.model.Employees;
import com.ms.sw.user.model.ActionType;
import com.ms.sw.user.model.User;
import com.ms.sw.exception.employees.AddEmployeeException;
import com.ms.sw.exception.employees.EmployeesNotFoundException;
import com.ms.sw.employee.repo.EmployeeRepository;
import com.ms.sw.user.repo.UserRepository;
import com.ms.sw.user.service.ActivityLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
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


        List<EmployeeListResponse> employees = employeeRepository.getAllEmployeesByOwner(username);

        if (employees.isEmpty()) {
            return List.of();
        }

        return employees;
    }

    @Transactional
    public Employees addEmployee(AddEmployeeRequest addEmployeeRequest, User user) {

        try {
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
            employee.setUpdatedAt(LocalDate.now());
            employee.setUser(user);


            Employees savedEmployee = employeeRepository.save(employee);

            activityLogsService.logAction(
                    ActionType.ADD,
                    employee.getFirstName().concat(" "+employee.getLastName())
                    ,user.getUsername());

            return savedEmployee;

        } catch (DataIntegrityViolationException e) {

            throw new AddEmployeeException("Error: Employee data violates unique constraints (e.g., ID or Email already exists).");
        }

    }
    public EmployeeDetailsResponse getEmployeeByPersonalId(String personalId, String username) {


        EmployeeDetailsResponse employee = employeeRepository.findByPersonalIdAndOwner(personalId, username)
                .orElseThrow(() -> new EmployeesNotFoundException("Employee not found or unauthorized."));

        return employee;
    }

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

        activityLogsService.logAction(ActionType.UPDATE,request.firstName().concat(" "+request.lastName()),username);

        if (updatedCount == 0) {
            throw new EmployeesNotFoundException("Employee not found or unauthorized to update.");
        }
    }
}
