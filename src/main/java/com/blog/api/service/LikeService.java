package com.blog.api.service;

import com.blog.api.models.dto.LikeDTO;
import com.blog.api.models.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface LikeService {
    public LikeDTO toggleLike(Long userId, Long artId);
    public Set<UserDTO> getAllLikedUser(Long id);
    public boolean isLiked(Long userId, Long artId);
    public Integer getLengthOfArticle(Long artId);
}
