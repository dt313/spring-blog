package com.blog.api.configuration;

import com.blog.api.dto.principal.OAuth2UserInfo;
import com.blog.api.dto.principal.PrincipalDetails;
import com.blog.api.entities.Role;
import com.blog.api.entities.User;
import com.blog.api.exception.AppException;
import com.blog.api.exception.ErrorCode;
import com.blog.api.repository.RoleRepository;
import com.blog.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class CustomOauth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Map<String, Object> oAuth2UserAttributes = super.loadUser(userRequest).getAttributes();

        // Registration Id
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // attributes
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(registrationId, oAuth2UserAttributes);

        User  user =  getOrSave(oAuth2UserInfo);
        return new PrincipalDetails(user, oAuth2UserAttributes ,userNameAttributeName);
    }




    private User getOrSave(OAuth2UserInfo oAuth2UserInfo) {

        User user = userRepository.findByEmail(oAuth2UserInfo.getEmail()).orElse(null);
        if(Objects.nonNull(user)) {
            return user;
        }else {
            Role role = roleRepository.findById("USER").orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
            User newUser = oAuth2UserInfo.toEntity();
            newUser.setRoles(new HashSet<>());
            newUser.getRoles().add(role);
            return userRepository.save(newUser);
        }
    }

}
