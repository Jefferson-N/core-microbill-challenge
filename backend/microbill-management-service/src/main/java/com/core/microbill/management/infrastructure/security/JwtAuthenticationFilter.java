package com.core.microbill.management.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final RestTemplate restTemplate;
    
    @Value("${auth.service.url}")
    private String authServiceUrl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        

        String requestURI = request.getRequestURI();

        if (requestURI.startsWith("/internal/") || 
            requestURI.startsWith("/swagger-ui/") || 
            requestURI.startsWith("/v3/api-docs") ||
            requestURI.startsWith("/actuator/")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring(7);
                
                HttpHeaders headers = new HttpHeaders();
                headers.set("Content-Type", "application/json");
                
                Map<String, String> requestBody = Map.of("token", token);
                HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
                
                ResponseEntity<Map> validationResponse = restTemplate.exchange(
                    authServiceUrl + "/api/auth/validate",
                    HttpMethod.POST,
                    entity,
                    Map.class
                );
                
                Map<String, Object> body = validationResponse.getBody();
                if (body != null && Boolean.TRUE.equals(body.get("valid"))) {
                    String username = (String) body.get("username");
                    UsernamePasswordAuthenticationToken authentication = 
                            new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (org.springframework.web.client.HttpClientErrorException e) {
                if (e.getStatusCode().value() == 401) {
                    log.warn("Token validation failed - Unauthorized: ", e.getMessage());
                } else {
                    log.error("Token validation error: ", e.getMessage());
                }
            } catch (org.springframework.web.client.ResourceAccessException e) {
                log.error("Auth service unavailable: ", e.getMessage());
            } catch (Exception e) {
                log.error("Unexpected error during token validation: ", e.getMessage(), e);
            }
        }

        
        filterChain.doFilter(request, response);
    }
}