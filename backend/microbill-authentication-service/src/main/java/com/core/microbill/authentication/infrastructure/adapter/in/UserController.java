package com.core.microbill.authentication.infrastructure.adapter.in;

import com.core.microbill.auth.api.UsersApi;
import com.core.microbill.auth.api.model.*;
import com.core.microbill.authentication.domain.model.User;
import com.core.microbill.authentication.domain.port.in.UserInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UsersApi {
    
    private final UserInputPort userInputPort;

    @Override
    public ResponseEntity<UserResponse> getUserById(Long id) {
        User user = userInputPort.findById(id);
        return ResponseEntity.ok(toResponse(user));
    }

    @Override
    public ResponseEntity<UserPageResponse> getUsers(Integer page, Integer size) {
        Page<User> users = userInputPort.findAll(PageRequest.of(page, size));
        
        UserPageResponse response = new UserPageResponse();
        response.setContent(users.getContent().stream().map(this::toResponse).toList());
        response.setTotalElements(users.getTotalElements());
        response.setTotalPages(users.getTotalPages());
        response.setSize(users.getSize());
        response.setNumber(users.getNumber());
        
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
