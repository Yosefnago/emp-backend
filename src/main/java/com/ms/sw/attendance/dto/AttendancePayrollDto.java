package com.ms.sw.attendance.dto;

import java.time.LocalDate;

public record AttendancePayrollDto(
        String personalId,
        LocalDate date,
        double totalHours,
        String status,
        boolean travelAllow
) {
}
