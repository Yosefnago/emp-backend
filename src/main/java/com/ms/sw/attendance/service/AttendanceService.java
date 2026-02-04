package com.ms.sw.attendance.service;

import com.ms.sw.attendance.dto.AttendanceDto;
import com.ms.sw.attendance.dto.EmployeeOptionDto;
import com.ms.sw.attendance.repo.AttendanceRepository;
import com.ms.sw.config.customUtils.CurrentUser;
import com.ms.sw.user.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public List<AttendanceDto> getAllRecords(String username,String year, String month,String department,String employeeName) {
        int y = Integer.parseInt(year);
        int m = Integer.parseInt(month);

        LocalDate startDate = LocalDate.of(y, m, 1);
        LocalDate endDate   = startDate.plusMonths(1);

        List<AttendanceDto> listOfAllRecords = attendanceRepository.getAllRecords(username,startDate,endDate,department,employeeName);

        return listOfAllRecords;
    }
    public List<EmployeeOptionDto> getMapOfEmployees(String username) {
        return attendanceRepository.loadMapOfEmployees(username);
    }

}
