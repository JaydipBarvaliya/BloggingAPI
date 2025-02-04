package com.blogging.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@Lazy
public class JwtUtil {

    // TODO: Move this secret and expiration_time into the application configuration.
    // @Value("${jwt.secret}")
    private final String SECRET_KEY = "***REMOVED***";

    // @Value("${jwt.expiration}")
    private final long EXPIRATION_TIME = 86400000;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(String email, String role, String firstName, String lastName, String authType, Long userId) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .claim("firstName", firstName)
                .claim("lastName", lastName)
                .claim("authType", authType)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Use getSigningKey() here for consistency
    public boolean validateToken(String token, String username) {
        String extractedUsername = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return extractedUsername.equals(username);
    }


    // Use getSigningKey() for extracting claims
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    // Method to extract username (email) from the token
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Method to extract role from the token
    public String extractRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }
}
