package com.ms.sw.attendance.dto;

public record AttendanceStatsDto(
        long presentDays,
        long sickDays,
        long vacationDays,
        double attendanceRate
) {}
