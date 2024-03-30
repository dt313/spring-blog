package com.blog.api.service;

import com.blog.api.exception.NotFoundException;
import com.blog.api.models.dto.UserDTO;
import com.blog.api.models.entity.User;
import com.blog.api.models.mapper.UserMapper;
import com.blog.api.repository.ArticleRepository;
import com.blog.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserServiceImp implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> result = new ArrayList<>();
        for (User user :  users) {
                System.out.println(user);
                result.add(UserMapper.toUserDTO(user));
        }
        return  result;

    }

    @Override
    public UserDTO getUser(Long id) {
        Optional<User> foundedUser = userRepository.findById(id);
        UserDTO result = new UserDTO();
        if (foundedUser.isPresent()) {
            result = UserMapper.toUserDTO(foundedUser.get());
            return result;
        }else
            throw new NotFoundException("Query user by id failed : id = " + id);

    }

    @Override
    public UserDTO insertUser(User newUser) {
        List<User>  isRegisted = userRepository.findUserByUsername(newUser.getUsername().trim());
        UserDTO result = new UserDTO();
        if(isRegisted.size() > 0) {
            throw new NotFoundException("User is already taken : name = " + newUser.getUsername());
        }else  {
            result = UserMapper.toUserDTO(userRepository.save(newUser));
            return result;
        }

    }

    @Override
    public UserDTO updateUser( Long id, User updateUser) {
        UserDTO updatedUser = userRepository.findById(id).map(user -> {
            System.out.print("Hearer ");
            user.setEmail(updateUser.getEmail());
            user.setUsername(updateUser.getUsername());
            user.setLastName(updateUser.getLastName());
            user.setFirstName(updateUser.getFirstName());
            user.setPassword(updateUser.getPassword());
            return UserMapper.toUserDTO(userRepository.save(user));
        }).orElseGet(() -> {
            updateUser.setId(id);
            return UserMapper.toUserDTO(userRepository.save(updateUser));
        });

        return updatedUser;
    }

    @Override
    public boolean deleteUser(Long id) {
        boolean isExists = userRepository.existsById(id);
        if(isExists) {
            userRepository.deleteById(id);
            return true;
        }else {
            throw new NotFoundException("Not found with user id = " + id);
        }
    }
}
