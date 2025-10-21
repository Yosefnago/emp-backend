package com.ms.sw.Dto.user;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRegisterRequest(@NotBlank String username, @NotBlank String password, @Email String email) {
}
