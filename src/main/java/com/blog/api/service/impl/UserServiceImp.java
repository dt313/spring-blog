package com.blog.api.service.impl;

import com.blog.api.dto.request.ResetPasswordRequest;
import com.blog.api.dto.request.UserCreationRequest;
import com.blog.api.dto.request.UserUpdateRequest;
import com.blog.api.dto.response.AuthenticationResponse;
import com.blog.api.dto.response.UserResponse;
import com.blog.api.entities.Image;
import com.blog.api.entities.InvalidToken;
import com.blog.api.entities.Role;
import com.blog.api.entities.User;
import com.blog.api.exception.AppException;
import com.blog.api.exception.ErrorCode;
import com.blog.api.mapper.UserMapper;
import com.blog.api.repository.ImageRepository;
import com.blog.api.repository.InvalidTokenRepository;
import com.blog.api.repository.RoleRepository;
import com.blog.api.repository.UserRepository;
import com.blog.api.service.UserService;
import com.blog.api.utils.ImageUtils;
import com.blog.api.utils.OtpUtils;
import com.blog.api.utils.TokenProvider;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImp implements UserService {

     UserRepository userRepository;
     UserMapper userMapper;
     PasswordEncoder passwordEncoder;
     RoleRepository roleRepository;
     TokenProvider tokenProvider;
     OtpUtils otpUtils;
     ImageRepository imageRepository;
     InvalidTokenRepository invalidTokenRepository;

    @NonFinal
    @Value("${server.domain}")
    String BASIC_IMAGE_URL;
    @Override
    public UserResponse getMyInformation() {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return userMapper.toUserResponse(user);

    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return  users.stream().map(userMapper::toUserResponse).toList();

    }

    @Override
    public UserResponse getUserByUsername(String username) {

        Optional<User> foundedUser = userRepository.findByUsername(username);
        if (foundedUser.isPresent()) {
            return userMapper.toUserResponse(foundedUser.get());
        }else
            throw new AppException(ErrorCode.USER_NOT_FOUND);
    }


    @Override
    public UserResponse createUser(UserCreationRequest newUser) {
        boolean isUserExist = userRepository.existsByEmail(newUser.getEmail());

        if(isUserExist) {
            throw new AppException(ErrorCode.USER_EXISTS);
        }

        if(newUser.getOtp().isEmpty() || !otpUtils.validateOTP(newUser.getEmail(), newUser.getOtp())) {
            throw new AppException(ErrorCode.OTP_INVALID);
        }else {

            String username = generateUsername(newUser.getEmail());
            newUser.setUsername(username);
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

            User user = userMapper.toUser(newUser);
            Role role = roleRepository.findById("USER").orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

            user.setRoles(new HashSet<>());
            user.getRoles().add(role);
            return userMapper.toUserResponse(userRepository.save(user));
        }
    }

    @Override
    public AuthenticationResponse updateUser(Long id, UserUpdateRequest updateUser, String token) throws ParseException, JOSEException {

        SignedJWT signToken = null;
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        String newToken = "";


        userMapper.updateUser(user, updateUser);
        if(!Objects.isNull(updateUser.getUsername())) {
                boolean isExist = userRepository.existsByUsername(updateUser.getUsername());
                if(isExist) throw new AppException(ErrorCode.USERNAME_EXISTED);
        }

        newToken = tokenProvider.generateToken(user);

        signToken = tokenProvider.verifyToken(token, true);
        String jwtId = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();
        InvalidToken invalidToken = InvalidToken.builder()
                    .id(jwtId)
                    .expiryTime(expiryTime)
                    .build();
        invalidTokenRepository.save(invalidToken);



        return AuthenticationResponse.builder().authenticated(true).token(newToken).user(userMapper.toUserResponse(userRepository.save(user))).build();

    }

    @Override
    public AuthenticationResponse uploadAvatar(Long id, MultipartFile img) {
        try {

            User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));


            var imageToSave = Image.builder()
                    .name(img.getOriginalFilename())
                    .type(img.getContentType())
                    .imageData(ImageUtils.compressImage(img.getBytes()))
                    .build();

            var savedImg = imageRepository.save(imageToSave);
            String imgUrl = BASIC_IMAGE_URL + "/image/" + savedImg.getId();
            user.setAvatar(imgUrl);


            return AuthenticationResponse.builder().authenticated(true).token(null).user(userMapper.toUserResponse(userRepository.save(user))).build();

        }catch (IOException e) {
            return null;
        }
    }

    //    @PreAuthorize("hasRole('CREATE_POST')")
    @Override
    public boolean deleteUser(Long id) {
        boolean isExists = userRepository.existsById(id);
        if(isExists) {
            try{
                userRepository.deleteById(id);

            }catch (DataIntegrityViolationException e) {
                throw new DataIntegrityViolationException(e.getMessage());
            }
            return true;

        }else {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
    }

    @Override
    public void resetPassword(String token, ResetPasswordRequest password) throws ParseException, JOSEException {
        boolean isValid = tokenProvider.verifyResetPasswordToken(token);
        if(isValid) {
            SignedJWT signedJWT = SignedJWT.parse(token);
            String userEmail = signedJWT.getJWTClaimsSet().getSubject();

            User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

            if(!password.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(password.getPassword()));
                userRepository.save(user);
            }

        }

    }
    public String generateUsername (String email) {
        String result = email.split("@")[0].concat(UUID.randomUUID().toString().substring(0,5));
        return result;
    }
}
