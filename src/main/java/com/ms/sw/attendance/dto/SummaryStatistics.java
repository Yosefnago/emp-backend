package com.ms.sw.attendance.dto;

public record SummaryStatistics(
        String daysOfAttendance,
        String daysMessing,
        String daysWithIllness,
        String daysOfHoliday,
        String attendanceRate
) {
}
