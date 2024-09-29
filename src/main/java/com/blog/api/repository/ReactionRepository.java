package com.blog.api.repository;

import com.blog.api.entities.Reaction;
import com.blog.api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReactionRepository extends JpaRepository<Reaction,Long> {
    Reaction findByReactionTableIdAndReactedUser(Long reactionTableId, User userId);
    boolean existsByReactionTableIdAndReactedUser(Long reactionTableId, User userId);
    List<Reaction> findAllByReactionTableId(Long reactionTableId);
}
