package com.blog.api.configuration;

import com.blog.api.exception.AuthException;
import com.blog.api.utils.TokenProvider;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${jwt.signerKey}")
    private String signerKey;

    @Autowired
    private TokenProvider tokenProvider;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException {

        try {
            boolean isInValidToken = tokenProvider.isInValid(token);
            if(isInValidToken) {
                throw new AuthException("custom token invalid", new BadJwtException("Signed JWT rejected: Invalid signature"));
            }
        } catch (ParseException | JOSEException e) {
            throw new AuthException("custom token invalid", new BadJwtException("Signed JWT rejected: Invalid signature"));
        }

        // Khởi tạo NimbusJwtDecoder nếu chưa được khởi tạo
        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }
            return nimbusJwtDecoder.decode(token);

    }
}
