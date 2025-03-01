package com.blog.api.controller;

import com.blog.api.entities.ResponseObject;
import com.blog.api.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/mail")
public class MailController {

    EmailService emailService;


    @GetMapping("/otp")
    public ResponseEntity sendOtp(@RequestParam String to) {
        emailService.sendOpt(to);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK,"Send opt sucessfully", null));
    }

    @GetMapping("/reset-password-link")
    public ResponseEntity sendResetPasswordLink(@RequestParam String to) {
        emailService.sendResetPasswordToken(to);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK,"Send reset password link sucessfully", null));
    }
}
