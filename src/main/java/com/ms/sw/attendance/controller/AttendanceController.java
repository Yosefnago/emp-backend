package com.ms.sw.attendance.controller;

import com.ms.sw.attendance.dto.*;
import com.ms.sw.attendance.service.AttendanceService;
import com.ms.sw.config.customUtils.CurrentUser;
import com.ms.sw.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/attendance")
@Slf4j
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/records")
    public ResponseEntity<List<AttendanceDto>> getAllAttendance(@CurrentUser User user, @RequestBody SearchQuery request) {

        log.info("AttendanceController::getAllAttendance invoked by user {}", user.getUsername());
        var year = request.year();
        var month = request.month();
        var department = request.department();
        var employeeName = request.employeeName();

        var list = attendanceService.getAllRecords(user.getUsername(),year,month,department,employeeName);

        return ResponseEntity.ok().body(list);
    }
    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeOptionDto>> loadEmployeesMap(@CurrentUser User user) {

        log.info("AttendanceController::getAllEmployees invoked by user {}", user.getUsername());
        var list = attendanceService.getMapOfEmployees(user.getUsername());
        return ResponseEntity.ok().body(list);
    }

    @PutMapping("/{personalId}")
    public ResponseEntity<Void> updateAttendance(@CurrentUser User user,
                                                 @PathVariable String personalId ,
                                                 @RequestBody AttendanceDto attendanceDto) {
        log.info("AttendanceController::updateAttendance invoked by user {}", user.getUsername());

        attendanceService.update(user.getUsername(),personalId,attendanceDto);
        return ResponseEntity.noContent().build();
    }
}



