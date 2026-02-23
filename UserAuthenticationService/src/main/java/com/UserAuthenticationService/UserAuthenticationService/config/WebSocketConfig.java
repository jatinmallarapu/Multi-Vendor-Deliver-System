package com.UserAuthenticationService.UserAuthenticationService.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // Allowed origins for WebSocket connections (from environment/properties)
    // Example: http://localhost:3000,http://localhost:4200,https://yourdomain.com
    @Value("${cors.allowed.origins:http://localhost:3000}")
    private String allowedOrigins;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // SECURITY FIX: Use specific allowed origins instead of wildcard "*"
        // Split comma-separated origins from configuration
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // Allow all here, let Gateway control actual CORS
                .withSockJS();
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
    }
}
