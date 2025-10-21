package com.ms.sw.service;

import com.ms.sw.Dto.employee.AddEmployeeRequest;
import com.ms.sw.Dto.employee.UpdateEmployeeDetailsRequest;
import com.ms.sw.entity.Employees;
import com.ms.sw.entity.User;
import com.ms.sw.exception.employees.AddEmployeeException;
import com.ms.sw.exception.employees.EmployeesNotFoundException;
import com.ms.sw.repository.EmployeeRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeesService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeesService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employees> getAllEmployees(String username) {

        List<Employees> employees = employeeRepository.getAllEmployeesByOwner(username);
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
            employee.setPosition(addEmployeeRequest.position());
            employee.setDepartment(addEmployeeRequest.department());
            employee.setHireDate(addEmployeeRequest.hireDate());
            employee.setStatus(addEmployeeRequest.status());
            employee.setCreatedAt(addEmployeeRequest.createdAt());
            employee.setUpdatedAt(addEmployeeRequest.updatedAt());
            employee.setUser(user);


            return employeeRepository.save(employee);


        } catch (RuntimeException e) {
            throw new AddEmployeeException("Error while adding employee ");
        }

    }
    public Employees getEmployeeByPersonalId(String personalId) {
        return employeeRepository.getEmployeesByPersonalId(personalId);
    }
    public void deleteEmployee(String id) {
        employeeRepository.deleteEmployeesByPersonalId(id);
    }

    @Transactional
    public void updateEmployeeDetails(@Valid UpdateEmployeeDetailsRequest updateEmployeeDetailsRequest, String username) {
        Employees emp = employeeRepository.findByPersonalIdAndOwner(updateEmployeeDetailsRequest.personal_id(),username)
                .orElseThrow(() -> new EmployeesNotFoundException("Employee not found"));

        emp.setFirstName(updateEmployeeDetailsRequest.firstName());
        emp.setLastName(updateEmployeeDetailsRequest.lastName());
        emp.setEmail(updateEmployeeDetailsRequest.email());
        emp.setPosition(updateEmployeeDetailsRequest.position());
        emp.setDepartment(updateEmployeeDetailsRequest.department());
        emp.setStatus(updateEmployeeDetailsRequest.status());

        employeeRepository.save(emp);
    }
}
