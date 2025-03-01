package com.blog.api.service;

import com.blog.api.dto.request.ResetPasswordRequest;
import com.blog.api.dto.request.UserCreationRequest;
import com.blog.api.dto.request.UserUpdateRequest;
import com.blog.api.dto.response.AuthenticationResponse;
import com.blog.api.dto.response.UserResponse;
import com.nimbusds.jose.JOSEException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;

@Service
public interface UserService {
//    @PreAuthorize("hasRole('ADMIN')")
List<UserResponse> getAllUsers();

//    @PostAuthorize("returnObject.username == authentication.name")
UserResponse getUserByUsername(String username);

    UserResponse createUser(UserCreationRequest newUser);

    AuthenticationResponse updateUser(Long id, UserUpdateRequest updateUser, String token) throws ParseException, JOSEException;

    AuthenticationResponse uploadAvatar(Long id, MultipartFile img);

    UserResponse getMyInformation();

    boolean deleteUser(Long id);

    void resetPassword(String token, ResetPasswordRequest password) throws ParseException, JOSEException;
}
