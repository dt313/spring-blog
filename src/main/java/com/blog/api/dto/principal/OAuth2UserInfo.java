package com.blog.api.dto.principal;

import com.blog.api.entities.User;
import com.blog.api.exception.AppException;
import com.blog.api.exception.ErrorCode;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Builder
@Data
public class OAuth2UserInfo {

    private String email;
    private String name;
    private String thumbnail;
    private String username;

    public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) { // registration id별로 userInfo 생성
            case "google" -> ofGoogle(attributes);
            case "github" -> ofGithub(attributes);
            default -> throw new AppException(ErrorCode.ILLEGAL_REGISTRATION_ID);

        };
    }

    private static OAuth2UserInfo  ofGoogle(Map<String, Object> attributes) {
        String username = attributes.get("email").toString().split("@")[0].concat(UUID.randomUUID().toString().substring(0,5));
        return OAuth2UserInfo.builder().name((String) attributes.get("name")).email((String) attributes.get("email"))
                .thumbnail((String) attributes.get("picture")).username(username).build();
    }

    private static OAuth2UserInfo  ofGithub(Map<String, Object> attributes) {
        String username = attributes.get("email").toString().split("@")[0].concat(UUID.randomUUID().toString().substring(0,5));
        return OAuth2UserInfo.builder().name((String) attributes.get("name")).username(username).email((String) attributes.get("email"))
                .thumbnail((String) attributes.get("avatar_url")).build();
    }

    public User toEntity() {

        User newUser = User.builder().name(name)
                .email(email)
                .username(username)
                .avatar(thumbnail)
                .build();


        return newUser;
    }
}
