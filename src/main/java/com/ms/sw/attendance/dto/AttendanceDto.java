package com.ms.sw.attendance.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record AttendanceDto(
        String personalId,
        LocalDate date,
        String name,
        LocalTime checkInTime,
        LocalTime checkOutTime,
        String status,
        boolean travelAllow,
        String notes
) {
}
