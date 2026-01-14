package com.ms.sw.employee.controller;

import com.dev.tools.Markers.ApiMarker;
import com.ms.sw.config.customUtils.CurrentUser;
import com.ms.sw.employee.dto.*;
import com.ms.sw.employee.service.ArchivedEmployeeService;
import com.ms.sw.user.model.User;
import com.ms.sw.employee.service.EmployeesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Employees", description = "employee controllers")
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/employees")
@Slf4j
@ApiMarker
public class EmployeeController {

    private final EmployeesService employeesService;
    private final ArchivedEmployeeService  archivedEmployeeService;
    @Autowired
    public EmployeeController(EmployeesService employeesService, ArchivedEmployeeService archivedEmployeeService) {
        this.employeesService = employeesService;
        this.archivedEmployeeService = archivedEmployeeService;
    }

    @GetMapping("/loadAll")
    public ResponseEntity<List<EmployeeListResponse>> getAllEmployees(@CurrentUser User user) {

        log.info("EmployeesController::getAllEmployees invoked by user '{}'", user.getUsername());
        List<EmployeeListResponse> employees = employeesService.getAllEmployees(user.getUsername());
        return ResponseEntity.ok(employees);
    }


    @GetMapping("/{personalId}")
    public ResponseEntity<EmployeeDetailsResponse> getEmployeeById(@CurrentUser User user, @PathVariable String personalId) {

        log.info("EmployeesController::getEmployeeById invoked by user '{}' for personalId '{}'", user.getUsername(), personalId);

        EmployeeDetailsResponse fullDetails = employeesService.getEmployeeByPersonalId(personalId, user.getUsername());

        return ResponseEntity.ok(fullDetails);
    }

    @PostMapping("/addEmployee")
    public ResponseEntity<AddEmployeeResponse> addEmployee(@CurrentUser User user, @RequestBody @Valid AddEmployeeRequest addEmployeeRequest) {

        log.info("EmployeesController::addEmployee invoked by user '{}'", user.getUsername());

        employeesService.addEmployee(addEmployeeRequest,user);
        return ResponseEntity.ok(new AddEmployeeResponse("Employee added successfully"));
    }

    @PutMapping("/updateEmployeeDetails/{personalId}")
    public ResponseEntity<UpdateEmployeeDetailsResponse> updateEmployeeDetails(
            @CurrentUser User user,
            @RequestBody @Valid UpdateEmployeeDetailsRequest updateEmployeeDetailsRequest,
            @PathVariable String personalId) {

        log.info("EmployeesController::updateEmployeeDetails invoked by user '{}'", user.getUsername());

        employeesService.updateEmployeeDetails(updateEmployeeDetailsRequest,user.getUsername(),personalId);
        return ResponseEntity.ok(new UpdateEmployeeDetailsResponse("Employee details updated"));
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEmployee(@CurrentUser User user, @PathVariable String id) {

        log.info("EmployeesController::archiveEmployee invoked by user '{}' for personalId '{}'", user.getUsername(), id);

        archivedEmployeeService.archiveEmployee(id, user.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/test")
    public ResponseEntity<?> testAuth(@CurrentUser User user) {

        log.info("EmployeesController::testAuth invoked by user '{}'", user.getUsername());

        return ResponseEntity.ok(Map.of("authenticatedUser", user.getUsername()));
    }
}
