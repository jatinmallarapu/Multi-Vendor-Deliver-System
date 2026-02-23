package com.ApiGateway.ApiGateway.Filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    //Purpose of RouteValidator.java: Determines which routes are open and which routes require authentication.

    //These endpoints do not need JWT token.
    public static final List<String> openApiEndpoints = List.of(
            "/api/v1/auth/signup",
            "/api/v1/auth/signin",
            "/api/v1/auth/citieslist",
            "/api/v1/auth/refresh",
            "/ws",
            "/ws/**"
    );

    /*
    Meaning:
    If the URL contains any openApiEndpoint → NOT SECURED -> No Token required
    Else → SECURED → Token required
     */
    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
