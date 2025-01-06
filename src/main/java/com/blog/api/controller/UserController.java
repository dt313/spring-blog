package com.blog.api.controller;

import com.blog.api.dto.request.UserCreationRequest;
import com.blog.api.dto.request.UserUpdateRequest;
import com.blog.api.dto.response.AuthenticationResponse;
import com.blog.api.dto.response.UserResponse;
import com.blog.api.entities.ResponseObject;
import com.blog.api.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ResponseObject> getMyInformation() {
        UserResponse me = userService.getMyInformation();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(1000, HttpStatus.OK, "Get information successfully", me)
        );
    }

    @GetMapping("")
    private ResponseEntity<ResponseObject> getAllUser() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK, "Get all user successfully", users ));
    }

    @GetMapping("/{username}")
    private ResponseEntity<ResponseObject> getUserByUsername(@PathVariable String username) {
        UserResponse foundedUser = userService.getUserByUsername(username);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK, "Get user by id successfully", foundedUser));
    }

    @PostMapping("")
    private ResponseEntity<ResponseObject> createUser(@RequestBody @Valid UserCreationRequest request) {
        UserResponse insertedUser = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(1000, HttpStatus.OK, "Create user successfully", insertedUser)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request) {

        System.out.println(request);
        AuthenticationResponse updatedUser = userService.updateUser(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK, "Update user successfully", updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteUserById(@PathVariable Long id) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        authentication.getAuthorities().forEach(grantedAuthority -> {
            log.info(grantedAuthority.getAuthority());
        });
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK, "Delete user successfully", ""));
    }



}
