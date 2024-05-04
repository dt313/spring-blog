package com.blog.api.service;

import com.blog.api.dto.request.AuthenticatedRequest;
import com.blog.api.dto.request.IntrospectRequest;
import com.blog.api.dto.request.LogoutRequest;
import com.blog.api.dto.response.AuthenticationResponse;
import com.blog.api.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticatedRequest request);
    IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException;
    public void  logout(LogoutRequest request) throws ParseException, JOSEException;

    AuthenticationResponse refresh(String authorization) throws ParseException, JOSEException;
}
