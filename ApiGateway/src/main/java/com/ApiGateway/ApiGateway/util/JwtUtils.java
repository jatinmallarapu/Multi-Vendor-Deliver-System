package com.ApiGateway.ApiGateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static io.jsonwebtoken.Jwts.builder;

@Component
public class JwtUtils {
    //Purpose of JwtUtils.java: Handles the actual JWT verification.

    /*
    This does 3 things:
    ->Verifies signature
    ->Checks token is not expired
    ->Validates token structure

    If anything fails → an exception is thrown → request rejected.
     */

    // JWT secret injected from application.properties (which reads from environment variable)
    // IMPORTANT: This MUST be the same secret used in UserAuthenticationService
    @Value("${jwt.secret}")
    private String SECRET;


    public void validateToken(final String token) {
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }


    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
