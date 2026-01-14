package com.ms.sw.employee.service;

import com.ms.sw.employee.model.ArchivedEmployees;
import com.ms.sw.employee.model.Employees;
import com.ms.sw.employee.repo.ArchivedEmployeeRepository;
import com.ms.sw.employee.repo.EmployeeRepository;
import com.ms.sw.exception.employees.EmployeesNotFoundException;
import com.ms.sw.user.model.ActionType;
import com.ms.sw.user.service.ActivityLogsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

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

    @Transactional
    public void archiveEmployee(String personalId, String username) {
        Optional<Employees> employeeOpt = employeeRepository.findByPersonalId(personalId);

        if (employeeOpt.isEmpty()) {
            throw new EmployeesNotFoundException("Employee not found or unauthorized to archive.");
        }

        Employees employee = employeeOpt.get();

        if (!employee.getUser().getUsername().equals(username)) {
            throw new EmployeesNotFoundException("Employee not found or unauthorized to archive.");
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
            archivedEmployee.setPhone(employee.getPhone());
            archivedEmployee.setAddress(employee.getAddress());
            archivedEmployee.setCity(employee.getCity());
            archivedEmployee.setCountry(employee.getCountry());
            archivedEmployee.setPosition(employee.getPosition());
            archivedEmployee.setDepartment(employee.getDepartment());
            archivedEmployee.setHireDate(employee.getHireDate());
            archivedEmployee.setJobType(employee.getJobType());
            archivedEmployee.setStatus(employee.getStatus());
            archivedEmployee.setUpdatedAt(employee.getUpdatedAt());

            archivedEmployee.setArchivedAt(Timestamp.from(Instant.now()));
            archivedEmployee.setArchivedBy(username);

            archivedEmployeeRepository.save(archivedEmployee);

            activityLogsService.logAction(ActionType.ARCHIVE,employee.getFirstName().concat(" "+employee.getLastName()),username);

            int deletedCount = employeeRepository.deleteByPersonalIdAndOwner(personalId, username);

            if (deletedCount == 0) {
                throw new EmployeesNotFoundException("Failed to delete employee from active table.");
            }


        } catch (Exception e) {
            throw new RuntimeException("Failed to archive employee: " + e.getMessage(), e);
        }
    }

}
