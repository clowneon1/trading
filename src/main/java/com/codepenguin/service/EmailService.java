package com.codepenguin.service;

import jakarta.mail.MessagingException;
import org.springframework.mail.javamail.JavaMailSender;

public interface EmailService {
    void sendVerificationOtpEmail(String email,String otp) throws MessagingException;
}
