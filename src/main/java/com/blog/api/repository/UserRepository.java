package com.blog.api.repository;

import com.blog.api.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findUserByUsername(String name);
}
