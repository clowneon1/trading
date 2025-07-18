package com.codepenguin.service;

import com.codepenguin.domain.VerificationType;
import com.codepenguin.model.User;
import com.codepenguin.model.VerificationCode;

public interface VerificationCodeService {
    VerificationCode sendVerificationCode(User user, VerificationType verificationType);
    VerificationCode getVerificationCodeById(Long id) throws Exception;
    VerificationCode getVerificationByUser(Long userId);
    void deleteVerificationCodeById(VerificationCode verificationCode);
    Boolean verifyOtp(String otp, VerificationCode verificationCode);
}
