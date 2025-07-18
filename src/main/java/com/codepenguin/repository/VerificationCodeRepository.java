package com.codepenguin.repository;

import com.codepenguin.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    VerificationCode findByUserId(Long userId);
}
