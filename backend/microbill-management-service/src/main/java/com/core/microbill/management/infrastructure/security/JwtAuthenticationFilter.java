package com.core.microbill.management.infrastructure.security;

import com.core.microbill.management.domain.exception.ValidationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final RestTemplate restTemplate;
    
    @Value("${auth.service.url}")
    private String authServiceUrl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring(7); // Remove "Bearer " prefix
                
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
            } catch (Exception e) {
                logger.debug("Token validation failed: " + e.getMessage());
                // No lanzar excepción, solo continuar sin autenticación
            }
        }
        filterChain.doFilter(request, response);
    }
}