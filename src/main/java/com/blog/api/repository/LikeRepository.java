package com.blog.api.repository;

import com.blog.api.models.entity.Article;
import com.blog.api.models.entity.Like;
import com.blog.api.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findLikeByArtId(Article artId);
}
