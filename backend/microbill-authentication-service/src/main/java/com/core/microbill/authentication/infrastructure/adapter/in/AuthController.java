package com.core.microbill.authentication.infrastructure.adapter.in;

import com.core.microbill.authentication.domain.model.User;
import com.core.microbill.authentication.domain.port.in.AuthInputPort;
import com.core.microbill.authentication.infrastructure.adapter.in.api.AuthApi;
import com.core.microbill.authentication.infrastructure.adapter.in.model.*;
import com.core.microbill.authentication.domain.port.in.TokenValidationInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthInputPort authInputPort;
    private final TokenValidationInputPort tokenValidationInputPort;

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        try {
            String token = authInputPort.login(request.getUsername(), request.getPassword());
            
            java.util.List<String> roles = tokenValidationInputPort.getRolesFromToken(token);
            
            LoginResponse response = new LoginResponse();
            response.setToken(token);
            response.setType("Bearer");
            response.setUsername(request.getUsername());
            response.setRoles(roles);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Override
    public ResponseEntity<UserResponse> register(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .roles(request.getRoles() != null ? new HashSet<>(request.getRoles()) : null)
                .build();

        User created = authInputPort.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @Override
    public ResponseEntity<TokenValidationResponse> validateToken(TokenValidationRequest tokenValidationRequest) {
        boolean isValid = tokenValidationInputPort.validateToken(tokenValidationRequest.getToken());
        String username = null;

        if (isValid) {
            username = tokenValidationInputPort.getUsernameFromToken(tokenValidationRequest.getToken());
        }

        TokenValidationResponse response = new TokenValidationResponse()
                .valid(isValid)
                .username(username);

        return ResponseEntity.ok(response);
    }

    private UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setEnabled(user.getEnabled());
        response.setRoles(user.getRoles().stream().toList());
        return response;
    }
}
