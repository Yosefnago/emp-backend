package com.ms.sw.controller;

import com.ms.sw.customUtils.CurrentUser;
import com.ms.sw.entity.User;
import com.ms.sw.service.EmployeesService;
import com.ms.sw.service.SalaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@Tag(name = "salaries", description = "salary controllers")
@RestController
@RequestMapping("/salary")
public class SalaryController {

    private final SalaryService salaryService;

    @Autowired
    public SalaryController(SalaryService salaryService){
        this.salaryService = salaryService;
    }

    @Operation(summary = "calculates all the salary of all the employees")
    @GetMapping("/salaries")
    public ResponseEntity<BigDecimal> calculateAllSalaries(@CurrentUser User user){

        BigDecimal total = salaryService.getAllSalaryOfAllEmployees(user.getUsername());
        return ResponseEntity.ok(total);
    }

    @Operation(summary = "calculate salary for employee by its personal id")
    @GetMapping("/{personalId}")
    public ResponseEntity<BigDecimal> calculateSalaryById(@PathVariable String personalId){

        double salary = salaryService.calculateSalary(personalId);

        return ResponseEntity.ok(new BigDecimal(salary));
    }


}
