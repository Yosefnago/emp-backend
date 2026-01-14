package com.ms.sw.employee.dto;

import com.dev.tools.Markers.DtoMarker;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.sql.Timestamp;


/**
 * {@code UpdateEmployeeDetailsRequest} is an immutable Data Transfer Object (DTO)
 * used to convey the necessary fields for modifying an existing employee's details via a REST API call.
 * <p>It enforces mandatory fields using Bean Validation (JSR 380) annotations to ensure
 * that critical identifying and update data are present and correctly formatted prior to
 * service layer processing.
 * @see jakarta.validation.constraints.NotBlank
 * @see jakarta.validation.constraints.Email
 */
@DtoMarker
public record UpdateEmployeeDetailsRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @Email String email,
        @NotBlank String personal_id,
        @NotBlank String gender,
        @NotBlank Timestamp birthDate,
        @NotBlank String familyStatus,
        @NotBlank String phone,
        @NotBlank String address,
        @NotBlank String city,
        @NotBlank String country,
        @NotBlank String position,
        @NotBlank String department,
        @NotBlank Timestamp hireDate,
        @NotBlank String jobType,
        @NotBlank String status
) {}
