package com.ms.sw.employee.controller;

import com.dev.tools.Markers.ApiMarker;
import com.ms.sw.config.customUtils.CurrentUser;
import com.ms.sw.employee.dto.*;
import com.ms.sw.employee.service.ArchivedEmployeeService;
import com.ms.sw.user.model.User;
import com.ms.sw.employee.service.EmployeesService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for managing employees.
 *
 * <p>Provides endpoints to list, retrieve, add, update, and archive employees.
 * Requires authentication, and actions are performed in the context of the
 * current authenticated user.</p>
 *
 */
@RestController
@RequestMapping("/employees")
@Slf4j
@ApiMarker
public class EmployeeController {

    private final EmployeesService employeesService;
    private final ArchivedEmployeeService  archivedEmployeeService;

    public EmployeeController(EmployeesService employeesService, ArchivedEmployeeService archivedEmployeeService) {
        this.employeesService = employeesService;
        this.archivedEmployeeService = archivedEmployeeService;
    }

    /**
     * Retrieves a list of all employees accessible by the authenticated user.
     *
     * @param user the current authenticated user
     * @return list of employees
     */
    @GetMapping()
    public ResponseEntity<List<EmployeeListResponse>> getAllEmployees(@CurrentUser User user) {

        log.info("GET /employees/ -> getAllEmployees -> user={}",user.getUsername());
        List<EmployeeListResponse> employees = employeesService.getAllEmployees(user.getUsername());
        return ResponseEntity.ok(employees);
    }

    /**
     * Retrieves detailed information for a specific employee by personal ID.
     *
     * @param user the current authenticated user
     * @param personalId the employee's personal ID
     * @return detailed employee information
     */
    @GetMapping("/{personalId}")
    public ResponseEntity<EmployeeDetailsResponse> getEmployeeById(@CurrentUser User user, @PathVariable String personalId) {

        log.info("GET /employees/{} -> getEmployeeById -> user={}",personalId,user.getUsername());

        EmployeeDetailsResponse fullDetails = employeesService.getEmployeeByPersonalId(personalId, user.getUsername());

        return ResponseEntity.ok(fullDetails);
    }

    /**
     * Adds a new employee.
     *
     * @param user the current authenticated user
     * @param addEmployeeRequest employee data to add
     * @return response indicating success
     */
    @PostMapping("/add")
    public ResponseEntity<AddEmployeeResponse> addEmployee(@CurrentUser User user, @RequestBody @Valid AddEmployeeRequest addEmployeeRequest) {

        log.info("POST /employees/add -> addEmployee -> user={}",user.getUsername());

        employeesService.addEmployee(addEmployeeRequest,user);
        return ResponseEntity.ok(new AddEmployeeResponse("Employee added successfully"));
    }

    /**
     * Updates details of an existing employee.
     *
     * @param user the current authenticated user
     * @param updateEmployeeDetailsRequest updated employee data
     * @param personalId the employee's personal ID
     * @return response indicating success
     */
    @PutMapping("/{personalId}")
    public ResponseEntity<UpdateEmployeeDetailsResponse> updateEmployeeDetails(
            @CurrentUser User user,
            @RequestBody @Valid UpdateEmployeeDetailsRequest updateEmployeeDetailsRequest,
            @PathVariable String personalId) {

        log.info("PUT /employees/{} -> updateEmployeeDetails -> user={}",personalId,user.getUsername());

        employeesService.updateEmployeeDetails(updateEmployeeDetailsRequest,user.getUsername(),personalId);
        return ResponseEntity.ok(new UpdateEmployeeDetailsResponse("Employee details updated"));
    }

    /**
     * Archives (soft deletes) an employee.
     *
     * @param user the current authenticated user
     * @param id the employee's personal ID
     * @return empty response indicating success
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@CurrentUser User user, @PathVariable String id) {

        log.info("DELETE /employees/{} -> deleteEmployee -> user={}",id,user.getUsername());

        archivedEmployeeService.archiveEmployee(id, user.getUsername());
        return ResponseEntity.ok().build();
    }

}
