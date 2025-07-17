package com.codepenguin.service;

import com.codepenguin.model.User;
import com.codepenguin.response.AuthResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<AuthResponse> save(User user) throws Exception;
    ResponseEntity<AuthResponse> login(User user) throws Exception;
    ResponseEntity<AuthResponse> verifyLoginOtp(String otp, String id) throws Exception;
}
