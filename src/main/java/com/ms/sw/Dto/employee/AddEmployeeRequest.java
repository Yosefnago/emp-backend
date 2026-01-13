package com.ms.sw.Dto.employee;

import com.dev.tools.Markers.DtoMarker;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.Date;

@DtoMarker
public record AddEmployeeRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String personalId,
        @Email String email,
        @NotBlank String phoneNumber,
        @NotBlank String address,
        @NotBlank String city,
        @NotBlank String country,
        @NotBlank String position,
        @NotBlank String department,
        @NotBlank Timestamp hireDate,
        @NotBlank String status
) {}
