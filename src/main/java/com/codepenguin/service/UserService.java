package com.codepenguin.service;

import com.codepenguin.model.User;
import com.codepenguin.response.AuthResponse;

public interface UserService {
    public AuthResponse save(User user) throws Exception;
}
