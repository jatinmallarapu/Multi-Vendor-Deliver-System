package com.ApiGateway.ApiGateway.config;

import com.ApiGateway.ApiGateway.Filter.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class GatewayConfig {

    @Autowired
    AuthenticationFilter authFilter;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder){
        return builder.routes()
                .route(p -> p.path("/get")
                        .uri("https://httpbin.org"))
                .route(p -> p.path("/ws/**")
                        .filters(f -> f.dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE")
                                       .dedupeResponseHeader("Access-Control-Allow-Credentials", "RETAIN_UNIQUE"))
                        .uri("lb://USERAUTHENTICATIONSERVICE"))
                .route(p->p.path("/api/v1/auth/**")
                        .filters(f->f.filter(authFilter.apply(new AuthenticationFilter.Config())))
                        .uri("lb://UserAuthenticationService"))
                .route(p -> p.path(
                                "/api/v1/user/**",
                                "/api/v1/admin/**",
                                "/api/v1/driver/**",
                                "/api/v1/owner/**")
                        .filters(f -> f.filter(authFilter.apply(new AuthenticationFilter.Config())))
                        .uri("lb://UserAuthenticationService"))
                .route(p -> p.path("/restaurant/**")
                        .filters(f -> f.filter(authFilter.apply(new AuthenticationFilter.Config())))
                        .uri("lb://RESTAURANTSERVICE"))
                .route(p -> p.path("/orders/**")
                        .filters(f -> f.filter(authFilter.apply(new AuthenticationFilter.Config())))
                        .uri("lb://ORDERMANAGEMENTSERVICE"))
                .route(p -> p.path("/delivery/**")
                        .filters(f -> f.filter(authFilter.apply(new AuthenticationFilter.Config())))
                        .uri("lb://DELIVERYANDLOGISTICSERVICE"))

                .build();
    }

    @Bean
    public CorsWebFilter corsWebFilter() {

        CorsConfiguration config = new CorsConfiguration();
                // Allow specific frontend origins and support credentials
                config.addAllowedOriginPattern("http://127.0.0.1:5500");
                config.addAllowedOriginPattern("http://localhost:5500");
                config.addAllowedHeader("*");   // allow all headers
                config.addAllowedMethod("*");   // allow GET, POST, PUT, DELETE, OPTIONS
                config.setAllowCredentials(true);
                config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}