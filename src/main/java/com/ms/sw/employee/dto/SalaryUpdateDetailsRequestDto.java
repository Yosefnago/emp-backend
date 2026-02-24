package com.ms.sw.employee.dto;

public record SalaryUpdateDetailsRequestDto(
        String personalId,
        String pensionFund,
        String providentFund,
        String insuranceCompany,
        double salaryPerHour,
        double seniority,
        double creditPoints,
        double totalSeekDays,
        double totalVacationDays
) {
}
