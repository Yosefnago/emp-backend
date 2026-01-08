package com.ms.sw.controller;

import com.dev.tools.Markers.ApiMarker;
import com.ms.sw.Dto.salaries.SalaryDetailsPerEmployee;
import com.ms.sw.customUtils.CurrentUser;
import com.ms.sw.entity.User;
import com.ms.sw.service.SalaryService;
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
@ApiMarker
public class SalaryController {

    private final SalaryService salaryService;

    @Autowired
    public SalaryController(SalaryService salaryService){
        this.salaryService = salaryService;
    }


    @GetMapping("/{personalId}")
    public ResponseEntity<BigDecimal> calculateSalaryById(@CurrentUser User user, @PathVariable String personalId){

        log.info("SalaryController::calculateSalaryById invoked by user '{}' for employee '{}'", user.getUsername(), personalId);
        double salary = salaryService.calculateSalary(personalId, user.getUsername());

        return ResponseEntity.ok(new BigDecimal(salary));
    }

    @GetMapping("/emp/{personalId}")
    public ResponseEntity<SalaryDetailsPerEmployee> getSalaryDetailsPerEmployee(@CurrentUser User user, @PathVariable String personalId){

        log.info("SalaryController::getSalaryDetailsPerEmployee invoked by user '{}' for employee '{}'", user.getUsername(), personalId);

        return ResponseEntity.ok(salaryService.getSalaryByPersonalId(personalId, user.getUsername()));
    }


}
