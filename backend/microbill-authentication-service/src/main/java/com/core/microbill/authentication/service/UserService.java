package com.core.microbill.authentication.service;

import com.core.microbill.authentication.domain.model.User;
import com.core.microbill.authentication.domain.port.in.UserInputPort;
import com.core.microbill.authentication.domain.port.out.UserOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserInputPort {
    
    private final UserOutputPort userOutputPort;

    @Override
    public User findById(Long id) {
        return userOutputPort.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userOutputPort.findAll(pageable);
    }
}
