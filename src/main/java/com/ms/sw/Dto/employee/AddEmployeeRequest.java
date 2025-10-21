package com.ms.sw.Dto.employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.sql.Timestamp;
import java.util.Date;

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

) {
}
