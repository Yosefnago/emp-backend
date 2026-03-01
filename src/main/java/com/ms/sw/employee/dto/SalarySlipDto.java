package com.ms.sw.employee.dto;

public record SalarySlipDto(
        Long id,
        int salaryYear,
        int salaryMonth,
        Double salaryAmount
) {}
