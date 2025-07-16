package com.codepenguin.service;

import com.codepenguin.model.User;
import com.codepenguin.response.AuthResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    public ResponseEntity<AuthResponse> save(User user) throws Exception;
    public ResponseEntity<AuthResponse> login(User user);
}
