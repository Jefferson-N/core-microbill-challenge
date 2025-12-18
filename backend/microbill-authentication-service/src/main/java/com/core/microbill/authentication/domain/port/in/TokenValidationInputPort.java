package com.core.microbill.authentication.domain.port.in;

public interface TokenValidationInputPort {
    boolean validateToken(String token);
    String getUsernameFromToken(String token);
    java.util.List<String> getRolesFromToken(String token);
}