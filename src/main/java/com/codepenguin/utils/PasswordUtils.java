package com.codepenguin.utils;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtils {

    private final PasswordEncoder passwordEncoder;

    public PasswordUtils(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String generateHash(String rawPassword){
        return passwordEncoder.encode(rawPassword);
    }

    public boolean matchPassword(String rawPassword, String hashedPassword){
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}

