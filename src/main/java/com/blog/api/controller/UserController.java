package com.blog.api.controller;

import com.blog.api.models.dto.UserDTO;
import com.blog.api.models.entity.ResponseObject;
import com.blog.api.models.entity.User;
import com.blog.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("")
    private ResponseEntity<ResponseObject> getAllUser() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Query all user successfully", users ));
    }

    @GetMapping("/{id}")
    private ResponseEntity<ResponseObject> getUserById(@PathVariable Long id) {
        UserDTO foundedUser = userService.getUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Query user by id successfully", foundedUser));
    }

    @PostMapping("")
    private ResponseEntity<ResponseObject> insertUser(@RequestBody User newUser) {
        UserDTO insertedUser = userService.insertUser(newUser);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(HttpStatus.OK, "insert succesfully", insertedUser)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateUser(@PathVariable Long id, @RequestBody User updateUser) {
        UserDTO updatedUser = userService.updateUser(id, updateUser);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "update successfully", updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteUserById(@PathVariable Long id) {
        boolean isExists = userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "delete user succesfully", ""));
    }

}
