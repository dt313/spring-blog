package com.blog.api.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig  {

    private final CustomOauth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    private final CustomJwtDecoder customJwtDecoder;

    private final String[] GET_METHOD_PUBLIC_ENDPOINTS = {
            "/api/v1/articles/**",
            "/api/v1/articles",
            "/api/v1/users/**",
            "/api/v1/topics",
            "/api/v1/reaction",
            "/api/v1/reaction/**",
            "/api/v1/bookmark/**",
            "/api/v1/bookmark",
            "/api/v1/comments/**",
            "/api/v1/image/**",
            "/static/**",
            "/api/v1/static/image/**",
            "/api/v1/mail/**"
    };

    private final String[] POST_METHOD_PUBLIC_ENDPOINTS = {
            "/api/v1/auth/login",
            "/api/v1/auth/logout",
            "/api/v1/auth/introspect",
            "/api/v1/auth/refresh",
            "/api/v1/users",
            "/api/v1/articles/suggestion",
    };

    private final String[] PUT_METHOD_PUBLIC_ENDPOINTS = {
            "/api/v1/users/reset-password"
    };

    @Value("${client.domain}")
    private String clientDomain;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

            httpSecurity.authorizeHttpRequests(request ->
                    request.requestMatchers(HttpMethod.POST, POST_METHOD_PUBLIC_ENDPOINTS).permitAll()
                            .requestMatchers(HttpMethod.GET, GET_METHOD_PUBLIC_ENDPOINTS).permitAll()
                            .requestMatchers(HttpMethod.PUT, PUT_METHOD_PUBLIC_ENDPOINTS).permitAll()
                            .requestMatchers("/api/v1/notification").permitAll()
                            .requestMatchers(HttpMethod.GET,"/api/v1/notifications").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/v1/users/me").authenticated()
                            .requestMatchers(HttpMethod.POST, "/api/v1/articles").authenticated()
//                            .requestMatchers(HttpMethod.GET, "/api/v1/users")
//                            .hasAuthority("ROLE_ADMIN")
                            .anyRequest()
                            .authenticated()
            );

            httpSecurity.oauth2Login(oauth2 -> oauth2.authorizationEndpoint(
                    b -> b.baseUri("/oauth2/authorization/**"))
                    .redirectionEndpoint(r -> r.baseUri("/oauth2/callback/**"))
                    .userInfoEndpoint(c-> c.userService(oAuth2UserService))
                    .successHandler(oAuth2SuccessHandler)
                    .failureHandler(oAuth2FailureHandler));

            httpSecurity.oauth2ResourceServer(
                    oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(customJwtDecoder)
                                    .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                    .authenticationEntryPoint(new JwtAuthenticationEntryPoint()));


            httpSecurity.csrf(AbstractHttpConfigurer::disable).cors(Customizer.withDefaults());
        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(clientDomain));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
