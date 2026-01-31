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
 * Interceptor for WebSocket connections that validates JWT tokens.
 * This ensures WebSocket connections are authenticated before establishing.
 */
@Component
@Slf4j
public class JwtHandshakeInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;

    public JwtHandshakeInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

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