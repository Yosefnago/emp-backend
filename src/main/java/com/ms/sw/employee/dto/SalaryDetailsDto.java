package com.ms.sw.employee.dto;

public record SalaryDetailsDto(
        double totalSeekDays,
        double totalVacationDays,
        double salaryPerHour,
        double seniority,
        double creditPoints
) {
}
