package com.blog.api.configuration;

import com.blog.api.dto.principal.PrincipalDetails;
import com.blog.api.entities.User;
import com.blog.api.utils.TokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private TokenProvider tokenProvider;

    @Value("${client.domain}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = principalDetails.getUser();
        String accessToken = tokenProvider.generateToken(user);
        String redirectUrl = UriComponentsBuilder.fromUriString( redirectUri+"/oauth2/redirect")
                .queryParam("accessToken", accessToken)
                .build().toUriString();
        response.sendRedirect(redirectUrl);
    }

}
