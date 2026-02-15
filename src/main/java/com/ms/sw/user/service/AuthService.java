package com.ms.sw.user.service;

import com.ms.sw.config.service.JwtService;
import com.ms.sw.exception.auth.InvalidCredentialsException;
import com.ms.sw.exception.auth.jwt.JwtInvalidException;
import com.ms.sw.user.dto.RefreshTokenResponse;
import com.ms.sw.user.dto.UserLoginRequest;
import com.ms.sw.user.dto.UserLoginResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service responsible for authentication and token lifecycle management.
 *
 * <p>This service encapsulates all authentication-related business logic,
 * including user credential validation, JWT generation, token refresh,
 * and refresh-token cookie management.</p>
 *
 * <p>The service is framework-aware (Servlet API) by design, as it manages
 * HTTP-only cookies for refresh tokens. No HTTP routing logic exists here;
 * all endpoints are handled by the controller layer.</p>
 *
 * <p>Responsibilities:</p>
 * <ul>
 *   <li>User authentication (username/password)</li>
 *   <li>Access token generation</li>
 *   <li>Refresh token issuance and validation</li>
 *   <li>Refresh token cookie management</li>
 * </ul>
 */
@Service
@Slf4j
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    private static final String REFRESH_TOKEN_COOKIE = "refreshToken";
    private static final int REFRESH_TOKEN_MAX_AGE = 7 * 24 * 60 * 60; //7 days in secondes

    public AuthService(UserService userService, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Authenticates a user and issues JWT tokens.
     *
     * <p>On successful authentication:</p>
     * <ul>
     *   <li>An access token is generated and returned</li>
     *   <li>A refresh token is generated and stored in an HTTP-only cookie</li>
     * </ul>
     *
     * <p>If credentials are invalid, authentication is considered failed
     * and token generation must not be trusted by callers.</p>
     *
     * @param request  login request containing username and password
     * @param response HTTP response used to attach the refresh token cookie
     * @return {@link UserLoginResponse} containing the access token and username
     */
    public UserLoginResponse login(UserLoginRequest request, HttpServletResponse response){

        log.info("Login attempt for user: {}", request.username());

        boolean isValid = userService.getUser(request.username())
                .map(user-> passwordEncoder.matches(request.password(), user.getPassword()))
                .orElse(false);

        if (!isValid){
            log.warn("Login failed: Invalid credentials for username: {}", request.username());
            throw new InvalidCredentialsException("Invalid credentials for username: " + request.username());
        }

        String accessToken = jwtService.generateAccessToken(request.username());
        String refreshToken = jwtService.generateRefreshToken(request.username());

        setRefreshTokenCookie(response, refreshToken);
        log.info("Login success for user: {}", request.username());

        return new UserLoginResponse(accessToken, request.username());
    }

    /**
     * Issues a new access token based on a valid refresh token.
     *
     * @param request HTTP request containing the refresh token cookie
     * @return {@link RefreshTokenResponse} containing a newly generated access token
     * @throws  JwtInvalidException if the refresh token is missing or invalid
     */
    public RefreshTokenResponse refresh(HttpServletRequest request) {
        String refreshToken = extractRefreshToken(request);

        if (refreshToken == null) {
            throw new JwtInvalidException("No refresh token provided");
        }

        jwtService.validateTokenNotExpired(refreshToken);

        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new JwtInvalidException("Invalid token type");
        }

        String username = jwtService.extractUsername(refreshToken);
        String newAccessToken = jwtService.generateAccessToken(username);

        return new RefreshTokenResponse(newAccessToken);
    }

    /**
     * Logs out the user by invalidating the refresh token.
     *
     * <p>This operation clears the refresh token cookie by setting its
     * maximum age to zero.</p>
     *
     * @param response HTTP response used to clear authentication cookies
     */
    public void logout(HttpServletResponse response) {
        clearRefreshTokenCookie(response);
    }

    /**
     * Stores the refresh token in an HTTP-only cookie.
     *
     * <p>The cookie is scoped to the application root path and has
     * a fixed expiration time.</p>
     *
     * @param response HTTP response
     * @param token    JWT refresh token to store
     */
    private void setRefreshTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // true בפרודקשן HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(REFRESH_TOKEN_MAX_AGE);
        response.addCookie(cookie);
    }

    /**
     * Extracts the refresh token from the HTTP-only cookie.
     *
     * @param request HTTP request
     * @return the refresh token value, or {@code null} if not present
     */
    private String extractRefreshToken(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if (REFRESH_TOKEN_COOKIE.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * Clears the refresh token cookie.
     *
     * <p>This is achieved by overwriting the cookie with a {@code null} value
     * and a maximum age of zero.</p>
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
    }
}
