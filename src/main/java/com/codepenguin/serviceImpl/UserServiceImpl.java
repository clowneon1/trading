package com.codepenguin.serviceImpl;

import com.codepenguin.config.JwtProvider;
import com.codepenguin.domain.VerificationType;
import com.codepenguin.model.TwoFactorAuth;
import com.codepenguin.model.User;
import com.codepenguin.repository.UserRepository;
import com.codepenguin.service.UserService;
import com.codepenguin.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordUtils passwordUtils;

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findUserById(Long id) throws Exception {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()){
            throw new Exception("User Not Found");
        }
        return user.get();
    }

    @Override
    public User findUserProfileByJwt(String jwt) throws Exception {
        String email = JwtProvider.getEmailFromToken(jwt);
        User user = findUserByEmail(email);
        if(user == null){
            throw new Exception("user not found");
        }
        return user;
    }

    @Override
    public User enableTwoFactorAuthentication(VerificationType verificationType, String sentTo, User user) {
        TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
        twoFactorAuth.setEnable(true);
        twoFactorAuth.setSendTo(verificationType);
        user.setTwoFactorAuth(twoFactorAuth);
        return userRepository.save(user);
    }


    @Override
    public User updatePassword(User user, String newPassword) {
        String newHashedPassword = passwordUtils.generateHash(newPassword);
        user.setPassword(newHashedPassword);
        return userRepository.save(user);
    }
}
