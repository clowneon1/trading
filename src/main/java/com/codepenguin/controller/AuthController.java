package com.codepenguin.controller;

import com.codepenguin.model.User;
import com.codepenguin.response.AuthResponse;
import com.codepenguin.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) throws Exception {
        return authService.save(user);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) throws Exception {
        return authService.login(user);
    }

    @PostMapping("/verify/{otp}")
    public ResponseEntity<AuthResponse> verifySigninOtp(
            @PathVariable String otp,
            @RequestParam String id) throws Exception {
        return authService.verifyLoginOtp(otp, id);
    }
}
