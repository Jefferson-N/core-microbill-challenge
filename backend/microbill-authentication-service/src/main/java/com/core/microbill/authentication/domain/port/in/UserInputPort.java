package com.core.microbill.authentication.domain.port.in;

import com.core.microbill.authentication.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserInputPort {
    User findById(Long id);
    Page<User> findAll(Pageable pageable);
}
