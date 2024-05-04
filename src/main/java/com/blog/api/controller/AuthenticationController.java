package com.blog.api.controller;

import com.blog.api.dto.request.AuthenticatedRequest;
import com.blog.api.dto.request.IntrospectRequest;
import com.blog.api.dto.request.LogoutRequest;
import com.blog.api.dto.response.AuthenticationResponse;
import com.blog.api.dto.response.IntrospectResponse;
import com.blog.api.entities.ResponseObject;
import com.blog.api.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE, makeFinal=true)

public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/introspect")
    ResponseEntity<ResponseObject> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        IntrospectResponse result = authenticationService.introspect(request);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "success", result));
    }
    @PostMapping("/login")
    ResponseEntity<ResponseObject> login(@RequestBody AuthenticatedRequest request) {
        var result = authenticationService.authenticate(request);
        return  ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "success", result));
    }

    @PostMapping("/logout")
    ResponseEntity<ResponseObject> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        System.out.println(request);
        authenticationService.logout(request);
        return  ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "logout success", ""));
    }


    @GetMapping("/refresh")
    ResponseEntity<ResponseObject> refresh(@RequestHeader Map<String, String> headers) throws ParseException, JOSEException {
        String authorization =  headers.get("authorization");
        AuthenticationResponse refreshToken = authenticationService.refresh(authorization);
        return  ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "refresh success", refreshToken));
    }



}
