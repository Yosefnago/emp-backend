package com.ms.sw.controller;

import com.ms.sw.Dto.employee.*;
import com.ms.sw.customUtils.CurrentUser;
import com.ms.sw.entity.Employees;
import com.ms.sw.entity.User;
import com.ms.sw.service.EmployeesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Tag(name = "Employees", description = "employee controllers")
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/employees")
public class EmployeeController {


    private final EmployeesService employeesService;

    @Autowired
    public EmployeeController(EmployeesService employeesService) {
        this.employeesService = employeesService;
    }

    @Operation(summary = "get all employees")
    @GetMapping("/loadAll")
    public List<Employees> getAllEmployees(@CurrentUser User user) {

        return employeesService.getAllEmployees(user.getUsername());
    }

    @Operation(summary = "get employee by id")
    @GetMapping("/{personalId}")
    public ResponseEntity<?> getEmployeeById(@PathVariable("personalId") String personalId) {

        Employees fullDetails = employeesService.getEmployeeByPersonalId(personalId);
        return ResponseEntity.ok(fullDetails);
    }

    @Operation(summary = "add employee")
    @PostMapping("/addEmployee")
    public ResponseEntity<AddEmployeeResponse> addEmployee(@CurrentUser User user, @RequestBody @Valid AddEmployeeRequest addEmployeeRequest) {

        employeesService.addEmployee(addEmployeeRequest,user);
        return ResponseEntity.ok(new AddEmployeeResponse("Employee added successfully"));
    }

    @Operation(summary = "update employee details")
    @PutMapping("/updateEmployeeDetails")
    public ResponseEntity<UpdateEmployeeDetailsResponse> updateEmployeeDetails(@CurrentUser User user, @RequestBody @Valid UpdateEmployeeDetailsRequest updateEmployeeDetailsRequest){
        employeesService.updateEmployeeDetails(updateEmployeeDetailsRequest,user.getUsername());

        return ResponseEntity.ok(new UpdateEmployeeDetailsResponse("Employee details updated"));
    }

    @Operation(summary = "delete employee by id")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String id) {

        employeesService.deleteEmployee(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "loads the number of employees that a user have")
    @GetMapping("/loadNumberOfEmployees")
    public int loadNumberOfEmployees(@CurrentUser User user) {
        return employeesService.loadNumberOfEmployees(user.getUsername());
    }

    @GetMapping("/test")
    public ResponseEntity<?> testAuth(@CurrentUser User user) {

        return ResponseEntity.ok(Map.of("authenticatedUser", user.getUsername()));
    }
}
