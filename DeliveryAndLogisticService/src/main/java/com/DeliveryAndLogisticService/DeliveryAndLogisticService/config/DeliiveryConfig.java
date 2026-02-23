package com.DeliveryAndLogisticService.DeliveryAndLogisticService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class DeliiveryConfig {
    // @Bean
    // public CorsWebFilter corsWebFilter() {
    //
    //     CorsConfiguration config = new CorsConfiguration();
    //     config.addAllowedOrigin("*");   // allow all origins
    //     config.addAllowedHeader("*");   // allow all headers
    //     config.addAllowedMethod("*");   // allow GET, POST, PUT, DELETE, OPTIONS
    //     config.setAllowCredentials(false);
    //
    //     UrlBasedCorsConfigurationSource source =
    //             new UrlBasedCorsConfigurationSource();
    //     source.registerCorsConfiguration("/**", config);
    //
    //     return new CorsWebFilter(source);
    // }
}
