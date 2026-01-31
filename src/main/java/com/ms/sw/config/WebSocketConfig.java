package com.ms.sw.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

/**
 * WebSocket configuration for the application.
 *
 * <p>Enables STOMP messaging over WebSocket, configures message broker,
 * STOMP endpoints, and applies JWT authentication for incoming connections.</p>
 *
 * <p>Responsibilities:</p>
 * <ul>
 *     <li>Enable WebSocket message broker with simple broker destinations</li>
 *     <li>Set application destination prefix for messages</li>
 *     <li>Register STOMP endpoints and configure allowed origins</li>
 *     <li>Apply {@link JwtHandshakeInterceptor} to inbound WebSocket messages for authentication</li>
 * </ul>
 */
@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    // Interceptor that validates JWT tokens on WebSocket connections.
    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;

    public WebSocketConfig(JwtHandshakeInterceptor jwtHandshakeInterceptor) {
        this.jwtHandshakeInterceptor = jwtHandshakeInterceptor;
    }

    /**
     * Configures message broker with topic and queue destinations.
     *
     * @param registry MessageBrokerRegistry for broker configuration
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
        log.info("Message broker configured");
    }

    /**
     * Registers STOMP endpoints for WebSocket connections and configures allowed origins.
     *
     * @param registry StompEndpointRegistry for endpoint configuration
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/server-status")
                .setAllowedOrigins("http://localhost:4200");

        log.info("StompEndpointRegistry configured");
    }

    /**
     * Configures the client inbound channel by adding JWT authentication interceptor.
     *
     * @param registration ChannelRegistration for client inbound messages
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(jwtHandshakeInterceptor);
    }

}
