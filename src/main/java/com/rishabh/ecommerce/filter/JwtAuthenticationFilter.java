package com.rishabh.ecommerce.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.rishabh.ecommerce.services.JwtService;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, java.io.IOException {
        String authHeader = request.getHeader("Authorization");

        // 1. Check if the Authorization header is missing or doesn't start with
        // "Bearer"
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extract JWT and Username
        final String jwt = authHeader.substring(7);
        try {
            final String username = jwtService.extractUsername(jwt);
            log.debug("JWT authentication: extracted username='{}'", username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                boolean valid = jwtService.isTokenValid(jwt, userDetails);
                log.debug("JWT token valid for user '{}': {}", username, valid);

                if (valid) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    log.debug("Set SecurityContext authentication for '{}' with authorities={}", username,
                            userDetails.getAuthorities());
                } else {
                    log.debug("Token validation failed for user '{}'", username);
                }
            }
        } catch (JwtException ex) {
            log.debug("Invalid JWT: {}", ex.getMessage());
            // Ignore invalid or malformed JWTs so permitAll endpoints still work.
        }

        // 6. Pass the request to the next filter
        filterChain.doFilter(request, response);
    }
}