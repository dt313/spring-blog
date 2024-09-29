package com.blog.api.service;

import com.blog.api.dto.request.UserCreationRequest;
import com.blog.api.dto.request.UserUpdateRequest;
import com.blog.api.dto.response.UserResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
//    @PreAuthorize("hasRole('ADMIN')")
List<UserResponse> getAllUsers();

//    @PostAuthorize("returnObject.username == authentication.name")
UserResponse getUserByUsername(String username);

    UserResponse createUser(UserCreationRequest newUser);

    UserResponse updateUser(Long id, UserUpdateRequest updateUser);

    UserResponse getMyInformation();

    boolean deleteUser(Long id);

}
