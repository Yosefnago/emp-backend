package com.ms.sw.service;

import com.ms.sw.Dto.employee.AddEmployeeRequest;
import com.ms.sw.Dto.employee.UpdateEmployeeDetailsRequest;
import com.ms.sw.entity.Employees;
import com.ms.sw.entity.User;
import com.ms.sw.exception.employees.AddEmployeeException;
import com.ms.sw.exception.employees.EmployeesNotFoundException;
import com.ms.sw.repository.EmployeeRepository;
import com.ms.sw.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class EmployeesService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    @Autowired
    public EmployeesService(EmployeeRepository employeeRepository,UserRepository userRepository) {
        this.userRepository =  userRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<Employees> getAllEmployees(String username) {

        log.info("EmployeesService::getAllEmployees invoked by user '{}'", username);

        List<Employees> employees = employeeRepository.getAllEmployeesByOwner(username);

        log.info("EmployeesService::getAllEmployees returned {} employees for user '{}'");
        if (employees.isEmpty()) {
            return List.of();
        }
        return employees;
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
            employee.setPosition(addEmployeeRequest.position());
            employee.setDepartment(addEmployeeRequest.department());
            employee.setHireDate(addEmployeeRequest.hireDate());
            employee.setStatus(addEmployeeRequest.status());
            employee.setCreatedAt(addEmployeeRequest.createdAt());
            employee.setUpdatedAt(addEmployeeRequest.updatedAt());
            employee.setUser(user);

            log.info("EmployeesService::addEmployee successfully saved employee for user '{}'",
                     user.getUsername());

            return employeeRepository.save(employee);


        } catch (RuntimeException e) {
            log.error("EmployeesService::addEmployee failed for user '{}': {}",
                    user.getUsername(), e.getMessage());
            throw new AddEmployeeException("Error while adding employee ");
        }

    }
    public Employees getEmployeeByPersonalId(String personalId) {

        log.info("EmployeesService::getEmployeeByPersonalId invoked with personalId '{}'", personalId);
        return employeeRepository.getEmployeesByPersonalId(personalId);
    }
    public void deleteEmployee(String id) {

        log.info("EmployeesService::deleteEmployee invoked with personalId '{}'", id);
        employeeRepository.deleteEmployeesByPersonalId(id);
    }

    @Transactional
    public void updateEmployeeDetails(@Valid UpdateEmployeeDetailsRequest updateEmployeeDetailsRequest, String username) {

        log.info("EmployeesService::updateEmployeeDetails invoked by user '{}' for personalId '{}'",
                username, updateEmployeeDetailsRequest.personal_id());

        Employees emp = employeeRepository.findByPersonalIdAndOwner(updateEmployeeDetailsRequest.personal_id(),username)
                .orElseThrow(() -> new EmployeesNotFoundException("Employee not found"));

        emp.setFirstName(updateEmployeeDetailsRequest.firstName());
        emp.setLastName(updateEmployeeDetailsRequest.lastName());
        emp.setEmail(updateEmployeeDetailsRequest.email());
        emp.setPosition(updateEmployeeDetailsRequest.position());
        emp.setDepartment(updateEmployeeDetailsRequest.department());
        emp.setStatus(updateEmployeeDetailsRequest.status());

        log.info("EmployeesService::updateEmployeeDetails successfully updated personalId '{}' for user '{}'",
                updateEmployeeDetailsRequest.personal_id(), username);
        employeeRepository.save(emp);
    }
    public int loadNumberOfEmployees(String username) {

        log.info("EmployeesService::loadNumberOfEmployees invoked by user '{}'", username);
        return userRepository.loadNumberOfEmployeesByUsername(username);
    }
}
