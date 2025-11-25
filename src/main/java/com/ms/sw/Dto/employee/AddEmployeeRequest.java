package com.ms.sw.Dto.employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.Date;

/**
 * {@code AddEmployeeRequest} is an immutable Data Transfer Object (DTO)
 * used to encapsulate the data required to register a new employee via a REST API call.
 * <p>It uses Bean Validation (JSR 380) annotations to enforce data quality and
 * contract requirements before processing in the service layer, preventing invalid data
 * from reaching the business logic.
 * @see jakarta.validation.constraints.NotBlank
 * @see jakarta.validation.constraints.Email
 */
public record AddEmployeeRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String personalId,
        @Email String email,
        @NotBlank String position,
        @NotBlank String department,
        @NotBlank Date hireDate,
        @NotBlank String status,
        @NotBlank Timestamp createdAt,
        @NotBlank Timestamp updatedAt

) {}
