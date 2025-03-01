package com.blog.api.utils;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class OtpUtils {
    private final RedisTemplate<String, String> redisTemplate;
    private static final SecureRandom random = new SecureRandom();
    private static final long OTP_EXPIRATION_MINUTES = 5;


    public String generateOTP(String email) {
        String otp = String.format("%06d", random.nextInt(1000000));

        redisTemplate.opsForValue().set(email, otp, OTP_EXPIRATION_MINUTES, TimeUnit.MINUTES);
        return otp;
    }

    public boolean validateOTP(String email , String inputOpt) {
        Object storedOtp = redisTemplate.opsForValue().get(email);
        if(storedOtp != null && storedOtp.equals(inputOpt)) {
            redisTemplate.delete(email);
            return true;
        }
        return false;
    }


}
