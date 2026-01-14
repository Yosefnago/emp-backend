package com.ms.sw.user.dto;

import com.dev.tools.Markers.DtoMarker;
import jakarta.validation.constraints.NotBlank;

/**
 * {@code UserLoginRequest} is an immutable Data Transfer Object (DTO)
 * used to securely transport the necessary credentials for a user authentication request.
 * <p>It is specifically designed as the contract for the login endpoint
 * (e.g., POST /auth/login), carrying only the user's identifying username and
 * their secret password. It utilizes {@code @NotBlank} validation to ensure
 * both fields are present and non-empty.
 * <p>Using a record for this purpose ensures that the sensitive data fields are
 * final and prevents accidental modification during transit or processing.
 * @see jakarta.validation.constraints.NotBlank
 */
@DtoMarker
public record UserLoginRequest(@NotBlank String username,@NotBlank String password) {
}
