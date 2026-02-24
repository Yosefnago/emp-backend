package com.ms.sw.user.dto;

public record PasswordUpdateRequest(
        String oldPass,
        String newPass,
        String newPassAgain
) {
}
