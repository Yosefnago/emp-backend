package com.ms.sw.user.controller;


import com.dev.tools.Markers.ApiMarker;
import com.ms.sw.exception.auth.jwt.JwtExpiredException;
import com.ms.sw.exception.auth.jwt.JwtInvalidException;
import com.ms.sw.user.dto.UserLoginRequest;
import com.ms.sw.config.service.JwtService;
import com.ms.sw.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Authentication controller handling login, token refresh, and logout.
 * Implements secure token management with separate access and refresh tokens.
 */
@RestController
@RequestMapping("auth")
@ApiMarker
@Slf4j
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    private static final String REFRESH_TOKEN_COOKIE = "refreshToken";
    private static final int REFRESH_TOKEN_MAX_AGE = 7 * 24 * 60 * 60; //7 days in secondes

    public AuthController(UserService userService, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Login endpoint that authenticates user and return token.
     * Return access token in response body and refresh token in http-only cookie
     *
     * @param userLoginRequest contains username and password
     * @param response HTTP response to set refresh token cookie
     * @return access token and user info
     */
    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody @Valid UserLoginRequest userLoginRequest, HttpServletResponse response){

        try{
            log.info("Login attempt for user: {}", userLoginRequest.username());

            boolean isValid = userService.getUser(userLoginRequest.username())
                    .map(user -> passwordEncoder.matches(userLoginRequest.password(),user.getPassword()))
                    .orElse(false);

            if (!isValid){
                log.warn("Login failed: Invalid credentials for username: {}", userLoginRequest.username());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid username or password"));
            }

            String accessToken = jwtService.generateAccessToken(userLoginRequest.username());
            String refreshToken = jwtService.generateRefreshToken(userLoginRequest.username());

            setRefreshTokenCookie(response,refreshToken);

            Map<String,String> responseBody  = new HashMap<>();
            responseBody.put("token", accessToken);
            responseBody.put("accessToken", accessToken);
            responseBody.put("username", userLoginRequest.username());

            log.info("Login success for user: {}", userLoginRequest.username());
            return ResponseEntity.ok(responseBody);

        } catch (Exception e) {
            log.error("Login error for username: {}", userLoginRequest.username(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Login failed"));
        }
    }
    /**
     * Refresh endpoint that issues a new access token using the refresh token.
     * Expects refresh token in HTTP-only cookie.
     *
     * @param request HTTP request containing refresh token cookie
     * @return new access token
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request) {
        try {
            // Extract refresh token from cookie
            String refreshToken = extractRefreshTokenFromCookie(request);

            if (refreshToken == null) {
                log.warn("Refresh attempt without refresh token cookie");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "No refresh token provided"));
            }

            // Validate refresh token is not expired
            jwtService.validateTokenNotExpired(refreshToken);

            // Verify it's actually a refresh token (not an access token)
            if (!jwtService.isRefreshToken(refreshToken)) {
                log.error("Attempted to use non-refresh token for refresh");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid token type"));
            }

            // Extract username and generate new access token
            String username = jwtService.extractUsername(refreshToken);
            String newAccessToken = jwtService.generateAccessToken(username);

            log.info("Token refreshed successfully for user: {}", username);
            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));

        } catch (JwtExpiredException e) {
            log.warn("Refresh failed: Refresh token expired - {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "refresh_token_expired", "message", "Please login again"));

        } catch (JwtInvalidException e) {
            log.error("Refresh failed: Invalid refresh token - {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "invalid_refresh_token", "message", "Invalid token"));

        } catch (Exception e) {
            log.error("Unexpected error during token refresh", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Token refresh failed"));
        }
    }
    /**
     * Logout endpoint that clears the refresh token cookie.
     *
     * @param response HTTP response to clear refresh token cookie
     * @return success message
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        try {
            // Clear refresh token cookie by setting max age to 0
            clearRefreshTokenCookie(response);

            log.info("User logged out successfully");
            return ResponseEntity.ok(Map.of("message", "Logged out successfully"));

        } catch (Exception e) {
            log.error("Error during logout", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Logout failed"));
        }
    }

    /**
     * Sets the refresh token in an HTTP-only cookie.
     *
     * @param response HTTP response
     * @param refreshToken JWT refresh token
     */
    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE, refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(REFRESH_TOKEN_MAX_AGE);

        response.addCookie(cookie);
        log.debug("Refresh token cookie set");
    }

    /**
     * Extracts refresh token from HTTP-only cookie.
     *
     * @param request HTTP request
     * @return refresh token string, or null if not found
     */
    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (REFRESH_TOKEN_COOKIE.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Clears the refresh token cookie by setting max age to 0.
     *
     * @param response HTTP response
     */
    private void clearRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);
        log.debug("Refresh token cookie cleared");
    }



}
