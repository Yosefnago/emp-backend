package com.ms.sw.config;

import com.ms.sw.config.service.JwtService;
import com.ms.sw.exception.auth.jwt.JwtExpiredException;
import com.ms.sw.exception.auth.jwt.JwtInvalidException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * WebSocket handshake interceptor that validates JWT tokens on connection.
 *
 * <p>This interceptor ensures that all incoming WebSocket CONNECT messages
 * contain a valid JWT in the "Authorization" header before establishing
 * the connection. It sets the authenticated user in the Spring Security
 * context and the STOMP session.</p>
 *
 * <p>Responsibilities:</p>
 * <ul>
 *     <li>Extract JWT from STOMP "Authorization" header</li>
 *     <li>Validate token expiration and integrity</li>
 *     <li>Set authenticated user in Spring Security context</li>
 *     <li>Throw security exceptions for expired or invalid tokens</li>
 * </ul>
 */
@Component
@Slf4j
public class JwtHandshakeInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;

    public JwtHandshakeInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Intercepts WebSocket messages before sending to the channel.
     * Validates CONNECT commands and sets authentication if the token is valid.
     *
     * @param message the WebSocket message
     * @param channel the message channel
     * @return the same message if validation succeeds
     * @throws SecurityException if token is missing, expired, or invalid
     */
    @Override
    public Message<?> preSend(@NonNull Message<?> message,@NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if(accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {

            try{
                String username = validateAndExtractUsername(accessor);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username,null,null);

                SecurityContextHolder.getContext().setAuthentication(authentication);
                accessor.setUser(authentication);

                log.info("WebSocket authentication successful for user: {}", username);

            } catch (JwtExpiredException e) {
                log.error("Authentication failed: Token expired - {}", e.getMessage());
                throw new SecurityException("Token expired: " + e.getMessage());

            } catch (JwtInvalidException e) {
                log.error("Authentication failed: Invalid token - {}", e.getMessage());
                throw new SecurityException("Invalid token: " + e.getMessage());

            } catch (Exception e) {
                log.error("WebSocket authentication failed: {}", e.getMessage());
                throw new SecurityException("Authentication failed: " + e.getMessage());
            }
        }
        return message;
    }

    /**
     * Validates the JWT token from the STOMP header and extracts the username.
     *
     * @param accessor STOMP header accessor
     * @return the username from the validated token
     * @throws SecurityException if Authorization header is missing or malformed
     * @throws JwtExpiredException if the token has expired
     * @throws JwtInvalidException if the token is invalid or username is missing
     */
    private String validateAndExtractUsername(StompHeaderAccessor accessor) {
        String authHeader = accessor.getFirstNativeHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.error("WebSocket connection missing Authorization header");
            throw new SecurityException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        jwtService.validateTokenNotExpired(token);

        String username = jwtService.extractUsername(token);

        if (username == null || username.trim().isEmpty()) {
            log.error("WebSocket token validation failed: Token missing or invalid username");
            throw new JwtInvalidException("Token missing or invalid username");
        }
        log.debug("WebSocket authentication successful for user: {}", username);
        return username;
    }
}