package com.ms.sw.config;

import com.ms.sw.exception.auth.jwt.JwtExpiredException;
import com.ms.sw.exception.auth.jwt.JwtInvalidException;
import com.ms.sw.user.model.User;
import com.ms.sw.config.service.JwtService;
import com.ms.sw.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Optional;

/**
 * JWT Authentication Filter that intercepts HTTP requests and validates JWT tokens.
 *
 * <p>This filter runs once per request and performs the following tasks:</p>
 * <ul>
 *     <li>Extracts JWT from the Authorization header</li>
 *     <li>Validates token expiration and integrity</li>
 *     <li>Authenticates the user in Spring Security context if token is valid</li>
 *     <li>Handles expired or invalid tokens by returning 401 responses</li>
 * </ul>
 *
 * <p>Delegates user retrieval to {@link UserService} and token validation
 * to {@link JwtService}.</p>
 */
@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserService userService;

    public JwtAuthFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // Extract JWT token from Authorization header
        String jwt = extractJwtFromRequest(request);

        if(jwt == null){
            log.debug("No JWT token found in request to: {}", request.getRequestURI());
            filterChain.doFilter(request,response);
            return;
        }

        try{
            authenticateWithToken(jwt,request);

            filterChain.doFilter(request,response);
        }
        catch (JwtExpiredException e){
            handleExpiredToken(response, e);
        }
        catch (JwtInvalidException e) {
            handleInvalidToken(response, e);
        }
        catch (Exception e) {
            handleUnexpectedError(response, e);
        }
    }

    /**
     * Extracts the JWT token from the "Authorization" header.
     *
     * @param request HTTP request
     * @return the JWT token string, or {@code null} if not present
     */
    public String extractJwtFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    /**
     * Authenticates the request using the provided JWT token.
     *
     * <p>If valid, sets the authenticated user in the Spring Security context.</p>
     *
     * @param token JWT token to validate
     * @param request HTTP request for context
     * @throws JwtExpiredException if token has expired
     * @throws JwtInvalidException if token is invalid or user not found
     */
    public void authenticateWithToken(String token, HttpServletRequest request) {

        jwtService.validateTokenNotExpired(token);

        String username = jwtService.extractUsername(token);
        log.debug("Token validation successful for user: {}", username);

        Optional<User> userOptional = userService.getUser(username);
        if(userOptional.isEmpty()){
            log.error("Token valid but user not found for username: {}", username);
            throw new JwtInvalidException("User not found for username: " + username);
        }

        User user = userOptional.get();

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(user, null,null);

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        log.info("Authentication successful for user: {}", username);

    }

    /**
     * Handles expired JWT tokens by returning a 401 response with error details.
     *
     * @param response HTTP response
     * @param e JwtExpiredException with details
     * @throws IOException if writing the response fails
     */
    private void handleExpiredToken(HttpServletResponse response, JwtExpiredException e) throws IOException {

        log.warn("Authentication failed: Token expired - {}", e.getMessage());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.setContentType("application/json");

        String jsonResponse  = String.format(
                "{\"error\": \"token_expired\", \"message\": \"%s\"}",
                e.getMessage().replace("\"","\\\"")
        );
        response.getWriter().write(jsonResponse );
    }

    /**
     * Handles invalid JWT tokens (bad signature, malformed, etc.)
     * by returning a 401 response with error details.
     *
     * @param response HTTP response
     * @param e JwtInvalidException with details
     * @throws IOException if writing the response fails
     */
    private void handleInvalidToken(HttpServletResponse response, JwtInvalidException e)
            throws IOException {
        log.error("Authentication failed: Invalid token - {}", e.getMessage());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        String jsonResponse = String.format(
                "{\"error\": \"token_invalid\", \"message\": \"%s\"}",
                e.getMessage().replace("\"", "\\\"")
        );

        response.getWriter().write(jsonResponse);
    }

    /**
     * Handles unexpected errors during token validation by returning a
     * generic 401 authentication failed response.
     *
     * @param response HTTP response
     * @param e Exception that occurred
     * @throws IOException if writing the response fails
     */
    private void handleUnexpectedError(HttpServletResponse response, Exception e)
            throws IOException {
        log.error("Unexpected error during JWT validation", e);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        String jsonResponse = "{\"error\": \"authentication_failed\", " +
                "\"message\": \"Authentication failed\"}";

        response.getWriter().write(jsonResponse);
    }
}
