package com.ms.sw.attendance.dto;

public record SearchQuery(
        String year,
        String month,
        String department,
        String employeeName
) {
}
