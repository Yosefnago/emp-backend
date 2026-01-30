package com.ms.sw.config;

import com.ms.sw.config.service.JwtService;
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

@Component
@Slf4j
public class JwtHandshakeInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;

    public JwtHandshakeInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.error("No Authorization header");
                throw new SecurityException("Missing or invalid Authorization header");
            }

            String token = authHeader.substring(7);

            try {
                String username = jwtService.extractUsername(token);

                if (username == null || username.isEmpty()) {
                    log.error("WebSocket authenticated failed: Invalid token");
                    throw new SecurityException("Invalid token");
                }

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, null);

                SecurityContextHolder.getContext().setAuthentication(authentication);
                accessor.setUser(authentication);

                log.info("Authentication successful,{} ", username);

            } catch (Exception e) {
                log.error("Authentication failed", e.getMessage());
                throw new SecurityException("Token validation failed: " + e.getMessage());
            }
        }

        return message;
    }
}