package com.blog.api.configuration;

import com.blog.api.exception.ErrorCode;
import com.blog.api.dto.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;


public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint  {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        Throwable cause = authException.getCause();
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;


        if (cause instanceof JwtValidationException) {
            // Lỗi JWT hết hạn hoặc không hợp lệ
            if (cause.getMessage().contains("expired")) {
                errorCode = ErrorCode.TOKEN_EXPIRED;  // Mã lỗi cho token hết hạn
            } else {
                errorCode = ErrorCode.UNAUTHENTICATED;  // Mã lỗi cho token không hợp lệ
            }
        } else if (cause instanceof BadJwtException) {
            errorCode = ErrorCode.TOKEN_INVALID;  // Mã lỗi cho token không hợp lệ
        } else {
            errorCode = ErrorCode.UNAUTHENTICATED;  // Mã lỗi chung cho các trường hợp khác
        }

        response.setStatus(errorCode.getStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        response.flushBuffer();


    }
}
