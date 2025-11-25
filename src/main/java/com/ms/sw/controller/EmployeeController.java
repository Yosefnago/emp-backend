package com.ms.sw.controller;

import com.ms.sw.Dto.employee.*;
import com.ms.sw.config.swagger.apiAnnotations.GetEmployeeByPersonalIdApi;
import com.ms.sw.customUtils.CurrentUser;
import com.ms.sw.entity.User;
import com.ms.sw.service.EmployeesService;
import io.swagger.v3.oas.annotations.Operation;
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
public class EmployeeController {


    private final EmployeesService employeesService;

    @Autowired
    public EmployeeController(EmployeesService employeesService) {

        this.employeesService = employeesService;
    }

    @GetMapping("/loadAll")
    public ResponseEntity<List<EmployeeListResponse>> getAllEmployees(@CurrentUser User user) {

        log.info("EmployeesController::getAllEmployees invoked by user '{}'", user.getUsername());
        List<EmployeeListResponse> employees = employeesService.getAllEmployees(user.getUsername());
        return ResponseEntity.ok(employees);
    }

    @GetEmployeeByPersonalIdApi
    @GetMapping("/{personalId}")
    public ResponseEntity<EmployeeDetailsResponse> getEmployeeById(@CurrentUser User user, @PathVariable("personalId") String personalId) {

        log.info("EmployeesController::getEmployeeById invoked by user '{}' for personalId '{}'", user.getUsername(), personalId);

        EmployeeDetailsResponse fullDetails = employeesService.getEmployeeByPersonalId(personalId, user.getUsername());

        return ResponseEntity.ok(fullDetails);
    }

    @Operation(summary = "add employee")
    @PostMapping("/addEmployee")
    public ResponseEntity<AddEmployeeResponse> addEmployee(@CurrentUser User user, @RequestBody @Valid AddEmployeeRequest addEmployeeRequest) {

        log.info("EmployeesController::addEmployee invoked by user '{}'", user.getUsername());

        employeesService.addEmployee(addEmployeeRequest,user);
        return ResponseEntity.ok(new AddEmployeeResponse("Employee added successfully"));
    }

    @Operation(summary = "update employee details")
    @PutMapping("/updateEmployeeDetails")
    public ResponseEntity<UpdateEmployeeDetailsResponse> updateEmployeeDetails(@CurrentUser User user, @RequestBody @Valid UpdateEmployeeDetailsRequest updateEmployeeDetailsRequest){

        log.info("EmployeesController::updateEmployeeDetails invoked by user '{}'", user.getUsername());

        employeesService.updateEmployeeDetails(updateEmployeeDetailsRequest,user.getUsername());
        return ResponseEntity.ok(new UpdateEmployeeDetailsResponse("Employee details updated"));
    }

    @Operation(summary = "delete employee by id")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEmployee(@CurrentUser User user, @PathVariable String id) {

        log.info("EmployeesController::deleteEmployee invoked by user '{}' for personalId '{}'", user.getUsername(), id);

        employeesService.deleteEmployee(id, user.getUsername());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "loads the number of employees that a user have")
    @GetMapping("/loadNumberOfEmployees")
    public int loadNumberOfEmployees(@CurrentUser User user) {

        log.info("EmployeesController::loadNumberOfEmployees invoked by user '{}'", user.getUsername());

        return employeesService.loadNumberOfEmployees(user.getUsername());
    }

    @GetMapping("/test")
    public ResponseEntity<?> testAuth(@CurrentUser User user) {

        log.info("EmployeesController::testAuth invoked by user '{}'", user.getUsername());

        return ResponseEntity.ok(Map.of("authenticatedUser", user.getUsername()));
    }
}
