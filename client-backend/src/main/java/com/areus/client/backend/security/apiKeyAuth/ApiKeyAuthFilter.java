package com.areus.client.backend.security.apiKeyAuth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Profile("api-key")
@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    @Value("${api.key.header}")
    private String apiKeyHeader;

    @Value("${api.key.secret}")
    private String apiKeySecret;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Get the API key and secret from request headers
        String requestApiSecret = request.getHeader(apiKeyHeader);

        log.info("Received {}: {}", apiKeyHeader, requestApiSecret);

        // Validate the key and secret
        if (apiKeySecret.equals(requestApiSecret)) {
            // Continue processing the request
            filterChain.doFilter(request, response);
        } else {
            log.info("API Key Authentication Failed!");
            // Reject the request and send an unauthorized error
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Unauthorized");
        }
    }
}