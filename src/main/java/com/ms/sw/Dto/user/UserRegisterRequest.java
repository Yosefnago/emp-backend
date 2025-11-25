package com.ms.sw.Dto.user;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * {@code UserRegisterRequest} is an immutable Data Transfer Object (DTO)
 * used to securely transport the essential data required for a new user to
 * register an account within the system.
 * <p>It is the explicit contract for the user registration endpoint (e.g., POST /auth/register).
 * The DTO enforces data quality using Bean Validation (JSR 380) annotations
 * to ensure that all mandatory credentials and information are provided and correctly formatted.
 * @param username The unique identifier chosen by the user for login. Must not be null or empty ({@code @NotBlank}).
 * @param password The user's chosen secret password. Must not be null or empty ({@code @NotBlank}).
 * @param email The user's primary email address. Must be a valid email format ({@code @Email}).
 * @see jakarta.validation.constraints.NotBlank
 * @see jakarta.validation.constraints.Email
 */
public record UserRegisterRequest(@NotBlank String username, @NotBlank String password, @Email String email) {
}
