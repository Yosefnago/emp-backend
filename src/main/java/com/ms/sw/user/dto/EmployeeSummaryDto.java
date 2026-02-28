package com.ms.sw.user.dto;

public record EmployeeSummaryDto(
        Long id,
        String firstName,
        String lastName,
        String position,
        String email
) {}
