package com.codepenguin.serviceImpl;

import com.codepenguin.domain.VerificationType;
import com.codepenguin.model.User;
import com.codepenguin.model.VerificationCode;
import com.codepenguin.repository.VerificationCodeRepository;
import com.codepenguin.service.VerificationCodeService;
import com.codepenguin.utils.OtpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private final VerificationCodeRepository verificationCodeRepository;

    @Override
    public VerificationCode sendVerificationCode(User user, VerificationType verificationType) {
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(OtpUtils.generateOtp());
        verificationCode.setVerificationType(verificationType);
        verificationCode.setUser(user);
        return verificationCodeRepository.save(verificationCode);
    }

    @Override
    public VerificationCode getVerificationCodeById(Long id) throws Exception {
        Optional<VerificationCode> verificationCode = verificationCodeRepository.findById(id);
        if(verificationCode.isPresent()){
            return verificationCode.get();
        }
        throw new Exception("Verification code not found");
    }

    @Override
    public VerificationCode getVerificationByUser(Long userId) {
        return verificationCodeRepository.findByUserId(userId);
    }

    @Override
    public void deleteVerificationCodeById(VerificationCode verificationCode) {
        verificationCodeRepository.delete(verificationCode);
    }

    @Override
    public Boolean verifyOtp(String otp, VerificationCode verificationCode) {
        return verificationCode.getOtp().equals(otp);
    }
}
