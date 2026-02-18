package com.ms.sw.employee.dto;

public record SalaryStatsDto(
        Double   totalSalary,
        Double   avgSalary,
        Double   maxSalary
) {
}
