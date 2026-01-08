package com.ms.sw.Dto.employee;

import com.dev.tools.Markers.DtoMarker;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * {@code UpdateEmployeeDetailsRequest} is an immutable Data Transfer Object (DTO)
 * used to convey the necessary fields for modifying an existing employee's details via a REST API call.
 * <p>It enforces mandatory fields using Bean Validation (JSR 380) annotations to ensure
 * that critical identifying and update data are present and correctly formatted prior to
 * service layer processing.
 * @see jakarta.validation.constraints.NotBlank
 * @see jakarta.validation.constraints.Email
 * @see com.ms.sw.controller.EmployeeController#updateEmployeeDetails(com.ms.sw.entity.User, UpdateEmployeeDetailsRequest)
 */
@DtoMarker
public record UpdateEmployeeDetailsRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @Email String email,
        @NotBlank String personal_id,
        @NotBlank String phone,
        @NotBlank String position,
        @NotBlank String department,
        @NotBlank String status
) {}
