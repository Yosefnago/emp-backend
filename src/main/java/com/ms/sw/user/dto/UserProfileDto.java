package com.ms.sw.user.dto;

public record UserProfileDto(
        String username,
        String email,
        String companyId,
        String companyName,
        String companyAddress,
        String phoneNumber
) {
}
