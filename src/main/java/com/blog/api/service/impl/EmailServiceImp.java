package com.blog.api.service.impl;


import com.blog.api.entities.User;
import com.blog.api.exception.AppException;
import com.blog.api.exception.ErrorCode;
import com.blog.api.repository.UserRepository;
import com.blog.api.service.EmailService;
import com.blog.api.service.UserService;
import com.blog.api.utils.OtpUtils;
import com.blog.api.utils.TokenProvider;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class EmailServiceImp implements EmailService {

    private JavaMailSender mailSender;
    private TokenProvider tokenProvider;
    private OtpUtils otpUtils;
    private UserRepository userRepository;

    @Override
    public void sendResetPasswordToken(String to) {

        User user = userRepository.findByEmail(to).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        String token = tokenProvider.generateResetPasswordToken(to);



        String link = "http://web-dev.com:3000/reset-password?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("FORGOT PASSWORD");
        message.setText(
                "Dear " + user.getUsername() + ",\n\n" +
                        "You have requested to reset your password. Please use the following token to proceed: " + link + "\n\n" +
                        "This token is valid for 10 minutes. Do not share it with anyone.\n\n" +
                        "If you did not request this, please ignore this email.\n\n" +
                        "Best regards,\n" +
                        "Your Company Name\n" +
                        "Contact: support@yourcompany.com"
        );
        mailSender.send(message);

    }

    public void sendOpt(String to) {
        String otp = otpUtils.generateOTP(to);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("OPT");
        message.setText(
                        "Dear Customer,\n\n" +
                        "Your One-Time Password (OTP) is: " + otp + "\n\n" +
                        "This OTP is valid for 10 minutes. Do not share it with anyone.\n\n" +
                        "If you did not request this, please ignore this email.\n\n" +
                        "Best regards,\n" +
                        "Your Company Name\n" +
                        "Contact: support@yourcompany.com"
        );
        mailSender.send(message);
    }
}
