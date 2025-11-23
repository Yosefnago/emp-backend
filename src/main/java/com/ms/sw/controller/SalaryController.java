package com.ms.sw.controller;

import com.ms.sw.Dto.salaries.SalaryDetailsPerEmployee;
import com.ms.sw.customUtils.CurrentUser;
import com.ms.sw.entity.User;
import com.ms.sw.service.SalaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(name = "salaries", description = "salary controllers")
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/salary")
@Slf4j
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

    @GetMapping("/emp/{personalId}")
    public ResponseEntity<SalaryDetailsPerEmployee> getSalaryDetailsPerEmployee(@PathVariable String personalId){
        log.info("EmployeesController::getAllEmployees invoked by employee '{}'", personalId);

        return ResponseEntity.ok(salaryService.getSalaryByPersonalId(personalId));
    }


}
