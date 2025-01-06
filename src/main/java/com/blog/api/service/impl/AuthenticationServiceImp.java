package com.blog.api.service.impl;

import com.blog.api.dto.request.AuthenticatedRequest;
import com.blog.api.dto.request.IntrospectRequest;
import com.blog.api.dto.request.LogoutRequest;
import com.blog.api.dto.request.RefreshRequest;
import com.blog.api.dto.response.AuthenticationResponse;
import com.blog.api.dto.response.IntrospectResponse;
import com.blog.api.entities.InvalidToken;
import com.blog.api.exception.AppException;
import com.blog.api.exception.ErrorCode;
import com.blog.api.mapper.UserMapper;
import com.blog.api.repository.InvalidTokenRepository;
import com.blog.api.repository.UserRepository;
import com.blog.api.service.AuthenticationService;
import com.blog.api.utils.TokenProvider;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationServiceImp implements AuthenticationService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    InvalidTokenRepository invalidTokenRepository;
    TokenProvider tokenProvider;
    UserMapper userMapper;
    @Override
    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        String token = request.getToken();

        boolean isValid = true;
        try {
            tokenProvider.verifyToken(token, false);
        }catch(AppException error) {
            isValid = false;
        }

        return IntrospectResponse.builder().valid(isValid).build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticatedRequest req) {

        var user = userRepository.findByEmail(req.getEmail()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        boolean isAuthenticated = passwordEncoder.matches(req.getPassword(), user.getPassword());

        if(!isAuthenticated) {
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        }

        String token = tokenProvider.generateToken(user);
        return AuthenticationResponse.builder().authenticated(isAuthenticated).token(token).user(userMapper.toUserResponse(user)).build();
    }

    @Override
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        SignedJWT signToken = null;
        try {
            signToken = tokenProvider.verifyToken(request.getToken(), true);
            String jwtId = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidToken invalidToken = InvalidToken.builder()
                    .id(jwtId)
                    .expiryTime(expiryTime)
                    .build();

            invalidTokenRepository.save(invalidToken);
        }catch (AppException error) {
        }
    }

    public AuthenticationResponse refresh(RefreshRequest request) throws ParseException, JOSEException {

        if(request.getToken().isEmpty()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        SignedJWT signedJWT = tokenProvider.verifyToken(request.getToken(), true);

        var jwtId = signedJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidToken invalidToken = InvalidToken.builder()
                .id(jwtId)
                .expiryTime(expiryTime)
                .build();

        invalidTokenRepository.save(invalidToken);

        var username = signedJWT.getJWTClaimsSet().getSubject();
        var user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        var refreshToken = tokenProvider.generateToken(user);

        return AuthenticationResponse.builder().token(refreshToken).authenticated(true).user(userMapper.toUserResponse(user)).build();
    }


}
