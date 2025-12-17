package com.core.microbill.authentication.domain.port.in;

import com.core.microbill.authentication.domain.model.User;

public interface AuthInputPort {
    String login(String username, String password);
    User register(User user);
}
