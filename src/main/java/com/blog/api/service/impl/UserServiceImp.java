package com.blog.api.service.impl;

import com.blog.api.dto.request.UserCreationRequest;
import com.blog.api.dto.request.UserUpdateRequest;
import com.blog.api.entities.Role;
import com.blog.api.exception.AppException;
import com.blog.api.exception.ErrorCode;
import com.blog.api.dto.response.UserResponse;
import com.blog.api.mapper.UserMapper;
import com.blog.api.entities.User;
import com.blog.api.repository.RoleRepository;
import com.blog.api.repository.UserRepository;
import com.blog.api.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImp implements UserService {

     UserRepository userRepository;
     UserMapper userMapper;
     PasswordEncoder passwordEncoder;
     RoleRepository roleRepository;

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
        log.info("In method get user by Id");
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

        String username = generateUsername(newUser.getEmail());
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        User user = userMapper.toUser(newUser);
        Role role = roleRepository.findById("USER").orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        user.setRoles(new HashSet<>());
        user.getRoles().add(role);
        return userMapper.toUserResponse(userRepository.save(user));


    }

    @Override
    public UserResponse updateUser(String id, UserUpdateRequest updateUser) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUser(user, updateUser);
        user.setPassword(passwordEncoder.encode(updateUser.getPassword()));

        var roles = roleRepository.findAllById(updateUser.getRoles());
        user.setRoles(new HashSet<>(roles));
        return userMapper.toUserResponse(userRepository.save(user));
    }

//    @PreAuthorize("hasRole('CREATE_POST')")
    @Override
    public boolean deleteUser(String id) {
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

    private String generateUsername (String email) {
        String result = email.split("@")[0].toString().concat(UUID.randomUUID().toString().substring(0,5));
        return result;
    }
}
