package com.codepenguin.serviceImpl;

import com.codepenguin.config.JwtProvider;
import com.codepenguin.model.TwoFactorOTP;
import com.codepenguin.model.User;
import com.codepenguin.repository.TwoFactorOtpRepository;
import com.codepenguin.repository.UserRepository;
import com.codepenguin.response.AuthResponse;
import com.codepenguin.service.TwoFactorOtpService;
import com.codepenguin.service.UserService;
import com.codepenguin.utils.OtpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CustomUserDetailsServiceImpl customUserDetailsService;
    private final TwoFactorOtpService twoFactorOtpService;

    @Override
    public ResponseEntity<AuthResponse> save(User user) throws Exception {

        User isEmailExists  = userRepository.findByEmail(user.getEmail());

        if(isEmailExists != null){
            throw new Exception("email is already registered with another account");
        }

        User newUser = new User();
        newUser.setFullName(user.getFullName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        User savedUser = userRepository.save(newUser);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword()
        );

        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = JwtProvider.generateToken(auth);

        AuthResponse response = new AuthResponse();
        response.setJwt(jwt);
        response.setStatus(true);
        response.setMessage("register success");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<AuthResponse> login(User user) {
        String username = user.getEmail();
        String password = user.getPassword();
        Authentication auth = authenticate(username, password);

        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = JwtProvider.generateToken(auth);

        User authUser = userRepository.findByEmail(username);

        if(user.getTwoFactorAuth().isEnable()){
            AuthResponse response = new AuthResponse();
            response.setMessage("Two factor auth is enabled");
            response.setTwoFactorAuthEnable(true);
            String otp = OtpUtils.generateOtp();

            TwoFactorOTP oldTwoFactorOtp = twoFactorOtpService.findByUser(authUser.getId());
            if(oldTwoFactorOtp != null){
                twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorOtp);
            }
            TwoFactorOTP newTwoFactorOTP = twoFactorOtpService.createTwoFactorOtp(authUser, otp, jwt);

            response.setSession(newTwoFactorOTP.getId());

            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        }

        AuthResponse response = new AuthResponse();
        response.setJwt(jwt);
        response.setStatus(true);
        response.setMessage("login success");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        if(userDetails == null){
            throw new BadCredentialsException("invalid username or password");
        }
        if(!password.equals(userDetails.getPassword())){
            throw new BadCredentialsException("invalid password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,password, userDetails.getAuthorities());
    }
}
