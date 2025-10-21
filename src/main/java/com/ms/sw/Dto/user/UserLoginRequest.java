package com.ms.sw.Dto.user;

import jakarta.validation.constraints.NotBlank;


public record UserLoginRequest(@NotBlank String username,@NotBlank String password) {
}
