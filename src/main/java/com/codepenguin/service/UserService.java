package com.codepenguin.service;

import com.codepenguin.model.User;

public interface UserService {
    User findUserByEmail(String email);
    User save(User user);
}
