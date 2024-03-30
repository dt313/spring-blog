package com.blog.api.service;

import com.blog.api.models.dto.UserDTO;
import com.blog.api.models.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    public List<UserDTO> getAllUsers();
    public UserDTO getUser(Long id);
    public UserDTO insertUser(User newUser);
    public UserDTO updateUser(Long id, User updateUser);

    public boolean deleteUser(Long id);

}
