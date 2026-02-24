package com.ms.sw.employee.controller;

import com.ms.sw.attendance.dto.AttendanceSummaryRequest;
import com.ms.sw.attendance.service.AttendanceService;
import com.ms.sw.config.customUtils.CurrentUser;
import com.ms.sw.employee.dto.SalaryDetailsDto;
import com.ms.sw.employee.dto.SalaryStatsDto;
import com.ms.sw.employee.dto.SalaryUpdateDetailsRequestDto;
import com.ms.sw.employee.service.SalaryService;
import com.ms.sw.employee.service.SalaryStatsService;
import com.ms.sw.user.model.User;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/salary")
@Slf4j
public class SalaryController {

    private final SalaryService salaryService;
    private final AttendanceService attendanceService;
    private final SalaryStatsService salaryStatsService;

    public SalaryController(SalaryService salaryService,AttendanceService attendanceService,SalaryStatsService salaryStatsService) {
        this.salaryService = salaryService;
        this.attendanceService = attendanceService;
        this.salaryStatsService = salaryStatsService;
    }

    /**
     * Calculate salary for employee via {@link AttendanceSummaryRequest}.
     * After calculation salary details saves in database.
     */
    @PostMapping("/payroll")
    @Transactional
    public ResponseEntity<Void> payroll(@CurrentUser User user, @RequestBody AttendanceSummaryRequest request){
        log.info("SalaryController::calculateSalary for employee {} date {}-{}",request.employeeName(),request.year(),request.month());

        salaryService.fetchSalaryData(user,request);
        attendanceService.updateAttendanceToClosed(user,request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{year}/{month}")
    public ResponseEntity<SalaryStatsDto> getSalaryStats(
            @CurrentUser User user,
            @PathVariable int year,
            @PathVariable int month) {

        log.info("SalaryController::getSalaryStats");

        SalaryStatsDto dto =
                salaryStatsService.getSalaryStats(user, year, month);

        return ResponseEntity.ok(dto);
    }
    @GetMapping("/details/{personalId}")
    public ResponseEntity<SalaryDetailsDto> getSalaryDetailsById(@CurrentUser User user, @PathVariable String personalId) {
        log.info("SalaryController::getSalaryDetailsById invoked by {}",user.getUsername());

        var res = salaryStatsService.getSalaryDetails(user.getUsername(), personalId);

        return ResponseEntity.ok(res);
    }
    @PutMapping("/update/{personalId}")
    public ResponseEntity<HttpStatus> updateSalaryDetails(
            @CurrentUser User user,
            @RequestBody SalaryUpdateDetailsRequestDto requestDto,
            @PathVariable String personalId) {

        log.info("SalaryController::updateSalaryDetails invoked by {}",user.getUsername());

        salaryStatsService.updateSalaryDetails(user.getUsername(), personalId, requestDto);

        return ResponseEntity.ok(HttpStatus.CREATED);
    }
}
