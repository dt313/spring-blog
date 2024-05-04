package com.blog.api.controller;

import com.blog.api.dto.request.UserCreationRequest;
import com.blog.api.dto.request.UserUpdateRequest;
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

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
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
                new ResponseObject(HttpStatus.OK, "success", me)
        );
    }

    @GetMapping("")
    private ResponseEntity<ResponseObject> getAllUser() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Query all user successfully", users ));
    }

    @GetMapping("/{id}")
    private ResponseEntity<ResponseObject> getUserById(@PathVariable String id) {
        UserResponse foundedUser = userService.getUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Query user by id successfully", foundedUser));
    }

    @PostMapping("")
    private ResponseEntity<ResponseObject> createUser(@RequestBody @Valid UserCreationRequest request) {
        UserResponse insertedUser = userService.createUser(request);
        System.out.println(insertedUser);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(HttpStatus.OK, "insert succesfully", insertedUser)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateUser(@PathVariable String id, @RequestBody UserUpdateRequest request) {
        UserResponse updatedUser = userService.updateUser(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "update successfully", updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteUserById(@PathVariable String id) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username ", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> {
            log.info(grantedAuthority.getAuthority());
        });
        boolean isExists = userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "delete user succesfully", ""));
    }



}
