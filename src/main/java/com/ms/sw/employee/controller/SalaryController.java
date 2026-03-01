package com.ms.sw.employee.controller;

import com.ms.sw.attendance.dto.AttendanceSummaryRequest;
import com.ms.sw.attendance.service.AttendanceService;
import com.ms.sw.config.customUtils.CurrentUser;
import com.ms.sw.employee.dto.SalaryDetailsDto;
import com.ms.sw.employee.dto.SalarySlipDto;
import com.ms.sw.employee.dto.SalaryStatsDto;
import com.ms.sw.employee.dto.SalaryUpdateDetailsRequestDto;
import com.ms.sw.employee.model.Salary;
import com.ms.sw.employee.service.SalaryService;
import com.ms.sw.employee.service.SalaryStatsService;
import com.ms.sw.user.model.User;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
        log.info("POST /salary/payroll -> payroll -> user={}",user.getUsername());

        salaryService.fetchSalaryData(user,request);
        attendanceService.updateAttendanceToClosed(user,request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{year:[0-9]+}/{month:[0-9]+}")
    public ResponseEntity<SalaryStatsDto> getSalaryStats(
            @CurrentUser User user,
            @PathVariable int year,
            @PathVariable int month) {

        log.info("GET /salary/{}/{} -> getSalaryStats", year, month);
        SalaryStatsDto dto = salaryStatsService.getSalaryStats(user, year, month);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/details/{personalId}")
    public ResponseEntity<SalaryDetailsDto> getSalaryDetailsById(@CurrentUser User user, @PathVariable String personalId) {
        log.info("GET /details/{} -> getSalaryDetailsById -> user={}",personalId,user.getUsername());

        var res = salaryStatsService.getSalaryDetails(user.getUsername(), personalId);

        return ResponseEntity.ok(res);
    }
    @PutMapping("/update/{personalId}")
    public ResponseEntity<HttpStatus> updateSalaryDetails(
            @CurrentUser User user,
            @RequestBody SalaryUpdateDetailsRequestDto requestDto,
            @PathVariable String personalId) {

        log.info("PUT /update/{} -> updateSalaryDetails -> user={}",personalId,user.getUsername());

        salaryStatsService.updateSalaryDetails(user.getUsername(), personalId, requestDto);

        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping("/slips/search")
    public ResponseEntity<List<SalarySlipDto>> searchSlips(
            @RequestParam String personalId,
            @RequestParam int year,
            @RequestParam int month) {
        log.info("GET /slips/search -> personalId={}, year={}, month={}", personalId, year, month);
        return ResponseEntity.ok(salaryStatsService.searchSalarySlips(personalId, year, month));
    }

    @GetMapping("/slips/view/{salaryId}")
    public ResponseEntity<Resource> viewSalaryPdf(@PathVariable Long salaryId) {
        return serveFile(salaryId, "inline");
    }

    @GetMapping("/slips/download/{salaryId}")
    public ResponseEntity<Resource> downloadSalaryPdf(@PathVariable Long salaryId) {
        return serveFile(salaryId, "attachment");
    }

    private ResponseEntity<Resource> serveFile(Long salaryId, String dispositionType) {
        try {
            Salary salary = salaryStatsService.getSalaryById(salaryId);
            String path = salary.getPathOfTlush();

            if (path == null || path.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new FileSystemResource(path);
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            String fileName = path.substring(path.lastIndexOf("/") + 1);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, dispositionType + "; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (Exception e) {
            log.error("Error serving file: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
