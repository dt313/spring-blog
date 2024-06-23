package com.blog.api.repository;

import com.blog.api.entities.Reaction;
import com.blog.api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReactionRepository extends JpaRepository<Reaction,String> {
    Reaction findByReactionTableIdAndReactedUser(String rtId, User userId);
    List<Reaction> findAllByReactionTableId(String rtId);
}
