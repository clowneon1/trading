package com.codepenguin.service;

import com.codepenguin.domain.VerificationType;
import com.codepenguin.model.User;

public interface UserService {
    User findUserByEmail(String email);
    User save(User user);
    User findUserById(Long id) throws Exception;
    User findUserProfileByJwt(String jwt) throws Exception;

    User enableTwoFactorAuthentication(VerificationType verificationType,
                                       String sentTo,
                                       User user);
    User updatePassword(User user, String newPassword);
}
