package com.ms.sw.Dto.employee;


import com.dev.tools.Markers.DtoMarker;
import java.sql.Timestamp;

/**
 * {@code EmployeeDetailsResponse} is an immutable Data Transfer Object (DTO)
 * used to convey the comprehensive personal and organizational details of a single employee
 * across the REST API boundary.
 * <p>This record serves as the explicit contract for fetching full employee data (e.g., via GET /employees/{personalId}),
 * ensuring decoupling between the public API structure and the internal JPA {@code Employees} entity.
 * @see com.ms.sw.entity.Employees
 * @see com.ms.sw.controller.EmployeeController
 * @see com.ms.sw.service.EmployeesService
 */
@DtoMarker
public record EmployeeDetailsResponse(
        String firstName,
        String lastName,
        String personalId,
        String email,
        String gender,
        Timestamp birthDate,
        String familyStatus,
        String phone,
        String position,
        String department,
        String address,
        String city,
        String country,
        Timestamp hireDate,
        String jobType,
        String status,
        Timestamp updatedAt
) {}
