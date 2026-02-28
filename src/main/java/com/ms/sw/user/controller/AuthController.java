package com.ms.sw.user.controller;


import com.dev.tools.Markers.ApiMarker;

import com.ms.sw.user.dto.RefreshTokenResponse;
import com.ms.sw.user.dto.UserLoginRequest;
import com.ms.sw.user.dto.UserRegisterRequest;
import com.ms.sw.user.dto.UserRegisterResponse;
import com.ms.sw.user.service.AuthService;
import com.ms.sw.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * REST controller responsible for authentication-related HTTP endpoints.
 *
 * <p>This controller exposes a thin web layer for:</p>
 * <ul>
 *   <li>User login</li>
 *   <li>Access token refresh</li>
 *   <li>User logout</li>
 * </ul>
 *
 * <p>All authentication business logic is delegated to {@link AuthService}.
 * The controller is intentionally kept free of domain logic and is responsible
 * only for HTTP request/response handling.</p>
 *
 * <p>Security model:</p>
 * <ul>
 *   <li>Access token is returned in the response body</li>
 *   <li>Refresh token is stored in an HTTP-only cookie</li>
 * </ul>
 */
@RestController
@RequestMapping("auth")
@ApiMarker
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    /**
     * Authenticates a user using username and password.
     *
     * <p>On successful authentication:</p>
     * <ul>
     *   <li>An access token is returned in the response body</li>
     *   <li>A refresh token is issued and stored as an HTTP-only cookie</li>
     * </ul>
     *
     * @param request  login request containing username and password
     * @param response HTTP response used to attach the refresh token cookie
     * @return authentication result containing access token and user identifier
     */
    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody @Valid UserLoginRequest request, HttpServletResponse response){
        log.info("POST /auth/login - user={}, ",request.username());
        return ResponseEntity.ok(authService.login(request, response));
    }

    /**
     * User register endpoint.
     *
     *  <p>On success:</p>
     *
     * <ul>Returns UserRegisterDto response</ul>
     * @param request user register request dto
     * @return UserRegisterResponse and success message.
     */
    @PostMapping("register")
    public ResponseEntity<UserRegisterResponse> register(@RequestBody UserRegisterRequest request){
        log.info("POST /auth/register - user={}, ",request.username());
        userService.register(request);
        return ResponseEntity.ok(new UserRegisterResponse(true,"User registered successfully"));
    }

    /**
     * Issues a new access token using a valid refresh token.
     *
     * <p>The refresh token is expected to be provided via an HTTP-only cookie.</p>
     *
     * @param request HTTP request containing the refresh token cookie
     * @return response containing a newly generated access token
     */
    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh(HttpServletRequest request) {
        log.info("POST /auth/refresh - user={}",request.getAuthType().toString());
        return ResponseEntity.ok(authService.refresh(request));
    }
    /**
     * Logs out the currently authenticated user.
     *
     * <p>This operation invalidates the refresh token by clearing
     * the corresponding HTTP-only cookie.</p>
     *
     * @param response HTTP response used to clear authentication cookies
     * @return logout confirmation message
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        log.info("POST /auth/logout - user={}",response.getHeaderNames().toString());
        authService.logout(response);
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

}
