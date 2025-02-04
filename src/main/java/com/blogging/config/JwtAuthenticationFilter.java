package com.blogging.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil; // JWT utility class

    // Constructor to inject the AuthenticationManager and JwtUtil
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil; // Inject JwtUtil using Spring's dependency injection
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getHeader("Authorization");  // Extract token from header

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);  // Remove "Bearer " prefix

            try {

                // Extract email and role from the token
                String email = jwtUtil.extractUsername(token); // Assuming JWT contains username (email)
                String role = jwtUtil.extractRole(token); // Extract the role

                if (role != null) {
                    role = role.replaceAll("[\\[\\]]", "");
                }

                if (email != null && jwtUtil.validateToken(token, email)) {
                    // Create an authentication object using the username and role
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            email, null, List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                    );

                    System.out.println("------------------------Extracted Role: " + role);
                    // Set authentication in the Security Context
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // Handle invalid or expired token
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired token.");
                return; // Stop further processing if token is invalid
            }
        }

        filterChain.doFilter(request, response); // Continue with the request processing
    }
}
