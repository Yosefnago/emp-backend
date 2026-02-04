package com.ms.sw.attendance.controller;

import com.ms.sw.attendance.dto.AttendanceDto;
import com.ms.sw.attendance.dto.EmployeeOptionDto;
import com.ms.sw.attendance.dto.SearchQuery;
import com.ms.sw.attendance.service.AttendanceService;
import com.ms.sw.config.customUtils.CurrentUser;
import com.ms.sw.user.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/records")
    public ResponseEntity<List<AttendanceDto>> getAllAttendance(@CurrentUser User user, @RequestBody SearchQuery request) {

        var year = request.year();
        var month = request.month();
        var department = request.department();
        var employeeName = request.employeeName();

        var list = attendanceService.getAllRecords(user.getUsername(),year,month,department,employeeName);

        return ResponseEntity.ok().body(list);
    }
    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeOptionDto>> loadEmployeesMap(@CurrentUser User user) {

        var list = attendanceService.getMapOfEmployees(user.getUsername());
        return ResponseEntity.ok().body(list);
    }



}
