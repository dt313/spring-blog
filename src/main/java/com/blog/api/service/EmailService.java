package com.blog.api.service;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendResetPasswordToken(String to);
    void sendOpt(String to);

}
