package com.blog.api.controller;

import com.blog.api.dto.request.AuthenticatedRequest;
import com.blog.api.dto.request.IntrospectRequest;
import com.blog.api.dto.request.LogoutRequest;
import com.blog.api.dto.request.RefreshRequest;
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
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE, makeFinal=true)

public class AuthenticationController {
    AuthenticationService authenticationService;
    OAuth2AuthorizedClientService clientService;

    @PostMapping("/introspect")
    public ResponseEntity<ResponseObject> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        IntrospectResponse result = authenticationService.introspect(request);
        return  ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK, "introspect success", result));
    }
    @PostMapping("/login")
    public ResponseEntity<ResponseObject> login(@RequestBody AuthenticatedRequest request) {
        var result = authenticationService.authenticate(request);
        return   ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK, "login success", result));
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseObject> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return   ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK, "logout success", ""));
    }


    @PostMapping("/refresh")
    public ResponseEntity<ResponseObject> refresh(@RequestBody RefreshRequest request) throws ParseException, JOSEException {
        AuthenticationResponse refreshToken = authenticationService.refresh(request);
        return   ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK, "refresh success", refreshToken));
    }




}
