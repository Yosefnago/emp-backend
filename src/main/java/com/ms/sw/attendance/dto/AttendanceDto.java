package com.ms.sw.attendance.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record AttendanceDto(
        Long id,
        LocalDate date,
        String name,
        String position,
        String department,
        LocalTime checkInTime,
        LocalTime checkOutTime,
        String status
) {
}
