package com.ms.sw.Dto.employee;


import com.dev.tools.Markers.DtoMarker;

/**
 * {@code EmployeeListResponse} is an immutable Data Transfer Object (DTO)
 * designed to carry a summary view of employee data.
 * <p>It is specifically intended for use in API endpoints that return a list of employees
 * (e.g., {@code GET /employees/loadAll}) where displaying full details is unnecessary.
 * This ensures the API contract is decoupled from the JPA entity and minimizes the
 * data payload to improve client-side loading and overall API performance.
 * <p>The fields included provide essential information required for a tabular or list view
 * (e.g., name, contact, department, and current status).
 * @see com.ms.sw.entity.Employees
 * @see com.ms.sw.controller.EmployeeController#getAllEmployees(com.ms.sw.entity.User)
 */
@DtoMarker
public record EmployeeListResponse(
        String personalId,
        String firstName,
        String lastName,
        String email,
        String phone,
        String department,
        String status
) {}