package com.ms.sw.Dto.employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


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
