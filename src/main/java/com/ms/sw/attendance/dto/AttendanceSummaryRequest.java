package com.ms.sw.attendance.dto;

public record AttendanceSummaryRequest(
        String personalId,
        String year,
        String month,
        String department,
        String employeeName
) {
}
