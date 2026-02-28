package com.ms.sw.user.dto;

import java.time.LocalDate;
import java.util.List;

public record DepartmentDetailsDto(
        LocalDate dateOfCreate,
        long annualPlacement,
        String departmentCode,
        String departmentManager,
        String departmentPhone,
        String departmentMail,
        List<EmployeeSummaryDto> employees
) {
}
