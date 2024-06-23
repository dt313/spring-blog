package com.blog.api.service.impl;

import com.blog.api.dto.request.AuthenticatedRequest;
import com.blog.api.dto.request.IntrospectRequest;
import com.blog.api.dto.request.LogoutRequest;
import com.blog.api.dto.response.AuthenticationResponse;
import com.blog.api.dto.response.IntrospectResponse;
import com.blog.api.entities.InvalidToken;
import com.blog.api.entities.User;
import com.blog.api.exception.AppException;
import com.blog.api.exception.ErrorCode;
import com.blog.api.repository.InvalidTokenRepository;
import com.blog.api.repository.UserRepository;
import com.blog.api.service.AuthenticationService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationServiceImp implements AuthenticationService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    InvalidTokenRepository invalidTokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNED_KEY;

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        String token = request.getToken();

        boolean isValid = true;
        try {
            verifyToken(token);
        }catch(AppException error) {
            isValid = false;

        }

        return IntrospectResponse.builder().valid(isValid).build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticatedRequest req) {
        System.out.println(req);
        var user = userRepository.findByEmail(req.getEmail()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        boolean isAuthenticated = passwordEncoder.matches(req.getPassword(), user.getPassword());

        if(!isAuthenticated) {
            throw new AppException(ErrorCode.WRONG_PASSWORKD);
        }

        String token = generateToken(user);
        return AuthenticationResponse.builder().authenticated(isAuthenticated).token(token).userId(user.getId()).build();
    }

    @Override
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        System.out.println(request);
        var signToken = verifyToken(request.getToken());

        String jwtId = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidToken invalidToken = InvalidToken.builder()
                .id(jwtId)
                .expiryTime(expiryTime)
                .build();

        invalidTokenRepository.save(invalidToken);
    }

    public AuthenticationResponse refresh(String request) throws ParseException, JOSEException {

        if(request.isEmpty()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        var token = request.substring(7);

        System.out.println("Token"+  token);
        var signedJWT = verifyToken(token);

        var jwtId = signedJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidToken invalidToken = InvalidToken.builder()
                .id(jwtId)
                .expiryTime(expiryTime)
                .build();

        invalidTokenRepository.save(invalidToken);

        var username = signedJWT.getJWTClaimsSet().getSubject();
        var user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        var refreshToken = generateToken(user);
        return AuthenticationResponse.builder().token(refreshToken).authenticated(true).build();
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("localhost")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header , payload);

        try {
            jwsObject.sign(new MACSigner(SIGNED_KEY.getBytes()));
            return jwsObject.serialize();

        }catch (JOSEException error) {
            log.error("Cannot create token ", error);
            throw new RuntimeException(error);
        }
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNED_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if(!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED);

        if(invalidTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if(!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if(!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission ->
                            stringJoiner.add(permission.getName()));
            });
        }
        return stringJoiner.toString();
    }
}
