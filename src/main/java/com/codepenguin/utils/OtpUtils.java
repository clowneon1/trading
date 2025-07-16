package com.codepenguin.utils;

import java.util.Random;

public class OtpUtils {
    private static final int OTP_LENGTH = 6;
    public static String generateOtp(){
        Random random = new Random();
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        for(int i = 0; i < OTP_LENGTH; i++){
            otp.append(random.nextInt(10));
        }
        return  otp.toString();
    }
}
