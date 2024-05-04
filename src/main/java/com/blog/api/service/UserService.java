package com.blog.api.service;

import com.blog.api.dto.request.UserCreationRequest;
import com.blog.api.dto.request.UserUpdateRequest;
import com.blog.api.dto.response.UserResponse;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
//    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers();

//    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(String id);

    UserResponse createUser(UserCreationRequest newUser);

    public UserResponse updateUser(String id, UserUpdateRequest updateUser);

    public UserResponse getMyInformation();

    public boolean deleteUser(String id);

}
