package com.ms.sw.attendance.service;

import com.ms.sw.attendance.dto.AttendanceDto;
import com.ms.sw.attendance.dto.EmployeeOptionDto;
import com.ms.sw.attendance.repo.AttendanceRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    private static final int DEFAULT_YEAR = 2026;
    private static final int DEFAULT_MONTH = 1;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public List<AttendanceDto> getAllRecords(String username,String year, String month,String department,String employeeName) {
        int y,m;

        if (year == null || year.isEmpty()){
            y = DEFAULT_YEAR;
        }else{
            y = Integer.parseInt(year);
        }
        if (month == null || month.isEmpty()){
            m = DEFAULT_MONTH;
        }else{
            m = Integer.parseInt(month);
        }

        LocalDate startDate = LocalDate.of(y, m, 1);
        LocalDate endDate   = startDate.plusMonths(1);

        return attendanceRepository.getAllRecords(username,startDate,endDate,department,employeeName);
    }

    public List<EmployeeOptionDto> getMapOfEmployees(String username) {
        return attendanceRepository.loadMapOfEmployees(username);
    }

    @Transactional
    public void update(String username,String personalId,AttendanceDto attendanceDto)  {

        attendanceRepository.updateOwnedRecord(
                personalId,
                username,
                attendanceDto.date(),
                attendanceDto.checkInTime(),
                attendanceDto.checkOutTime(),
                attendanceDto.status(),
                attendanceDto.notes()
        );

    }
}
