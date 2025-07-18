package com.codepenguin.controller;

import com.codepenguin.config.JwtConstant;
import com.codepenguin.domain.VerificationType;
import com.codepenguin.model.User;
import com.codepenguin.model.VerificationCode;
import com.codepenguin.service.EmailService;
import com.codepenguin.service.UserService;
import com.codepenguin.service.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final EmailService emailService;
    private final VerificationCodeService verificationCodeService;

    @GetMapping("/users/profile")
    public ResponseEntity<User> getUserByJwt(@RequestHeader(JwtConstant.JWT_HEADER) String jwt ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        return  new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/users/verification/{verificationType}/send-otp")
    public ResponseEntity<String> sendVerificationOtp(
            @RequestHeader(JwtConstant.JWT_HEADER) String jwt,
            @PathVariable VerificationType verificationType
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        VerificationCode verificationCode = verificationCodeService
                .getVerificationByUser(user.getId());

        if(verificationCode == null){
            verificationCode = verificationCodeService
                    .sendVerificationCode(user, verificationType);
        }

        if(verificationType.equals(VerificationType.EMAIL)){
            emailService.sendVerificationOtpEmail(user.getEmail(), verificationCode.getOtp());
        }


        return new ResponseEntity<>("verificaiton otp sent successfully", HttpStatus.OK);
    }

    @PatchMapping("/users/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<User> enableTwoFactorAuthentication(
            @RequestHeader(JwtConstant.JWT_HEADER) String jwt,
            @PathVariable String otp
            ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        VerificationCode verificationCode = verificationCodeService.getVerificationByUser(user.getId());
        String sendTo = verificationCode.getVerificationType().equals(VerificationType.EMAIL) ?
                verificationCode.getEmail() : verificationCode.getMobile();

        Boolean isVerified = verificationCodeService.verifyOtp(otp, verificationCode);

        if(isVerified){
            User updatedUser = userService.enableTwoFactorAuthentication(verificationCode.getVerificationType(), sendTo, user);
            verificationCodeService.deleteVerificationCodeById(verificationCode);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        throw new Exception("incorrect otp");
    }

}
