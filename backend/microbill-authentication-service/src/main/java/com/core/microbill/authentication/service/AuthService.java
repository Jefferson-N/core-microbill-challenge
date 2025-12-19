package com.core.microbill.authentication.service;

import com.core.microbill.authentication.domain.exception.BusinessLogicException;
import com.core.microbill.authentication.domain.exception.ValidationException;
import com.core.microbill.authentication.domain.model.User;
import com.core.microbill.authentication.domain.port.in.AuthInputPort;
import com.core.microbill.authentication.domain.port.out.UserOutputPort;
import com.core.microbill.authentication.infrastructure.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService implements AuthInputPort {
    
    private final UserOutputPort userOutputPort;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public String login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        
        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    public User register(User user) {
        if (!StringUtils.hasText(user.getUsername())) {
            throw new ValidationException("El nombre de usuario es requerido");
        }
        if (!StringUtils.hasText(user.getEmail())) {
            throw new ValidationException("El email es requerido");
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new ValidationException("La contraseña es requerida");
        }
        if (userOutputPort.existsByUsername(user.getUsername())) {
            throw new BusinessLogicException("Ya existe un usuario con el nombre " + user.getUsername());
        }
        if (userOutputPort.existsByEmail(user.getEmail())) {
            throw new BusinessLogicException("Ya existe un usuario con el email " + user.getEmail());
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(new HashSet<>());
            user.getRoles().add("USER");
        }
        
        return userOutputPort.save(user);
    }
}
