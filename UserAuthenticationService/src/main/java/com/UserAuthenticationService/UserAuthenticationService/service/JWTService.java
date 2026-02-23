package com.UserAuthenticationService.UserAuthenticationService.service;

import com.UserAuthenticationService.UserAuthenticationService.entities.User;
import com.UserAuthenticationService.UserAuthenticationService.entities.UserPrincipal;
import com.UserAuthenticationService.UserAuthenticationService.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {
    @Autowired
    UserRepository userRepository;

    // JWT secret injected from application.properties (which reads from environment variable)
    // This ensures the secret is not hardcoded in source code
    @Value("${jwt.secret}")
    private String secretKey;

    public JWTService() {

    }

    public String generateToken(String username) {
        User user = userRepository.findByEmail(username);
        UserPrincipal principal = new UserPrincipal(user);
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", principal.getAuthorities());
        return Jwts.builder()
                .claims()
                .add(claims)// You’re attaching the claims map (your payload data) to the token.
                .subject(username)// Adds a subject (sub) claim to the token as "sub": "jatin"
                .issuedAt(new Date(System.currentTimeMillis()))// "iat": <current timestamp>
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))// "exp": <timestamp 10 hours
                                                                                       // from now>
                .and()// for adding the signature we must you and()
                .signWith(getKey())// creates the digital signature with a key
                .compact();// this will generate token for you which is of String type
        // return
        // "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6Ik5hdmluIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.t_7L4lxVXO-BBRGBbUoK6q-hmXGbl6aYLSxiFJvnyjE";
    }

    public String generateRefreshToken(Map<String, Object> extraClaims, User user) {
        UserPrincipal principal = new UserPrincipal(user);
        extraClaims.put("roles", principal.getAuthorities());
        return Jwts.builder()
                .claims()
                .add(extraClaims)
                .subject(user.getEmail()) // or user.getUsername()
                .issuedAt(new Date(System.currentTimeMillis()))
                // refresh token validity (example: 7 days)
                .expiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))
                .and()
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
        // This will use the BASE64 Decoder and it will convert the String to byte array
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean validateRefreshToken(String token, String email) {
        String subject = extractUserName(token);
        return subject.equals(email) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
