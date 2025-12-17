package com.core.microbill.authentication.infrastructure.adapter.in;

import com.core.microbill.auth.api.AuthApi;
import com.core.microbill.auth.api.model.*;
import com.core.microbill.authentication.domain.model.User;
import com.core.microbill.authentication.domain.port.in.AuthInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    
    private final AuthInputPort authInputPort;

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        String token = authInputPort.login(request.getUsername(), request.getPassword());
        
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setType("Bearer");
        response.setUsername(request.getUsername());
        
        return ResponseEntity.ok(response);
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
