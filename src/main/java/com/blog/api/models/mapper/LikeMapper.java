package com.blog.api.models.mapper;

import com.blog.api.models.dto.ArticleDTO;
import com.blog.api.models.dto.LikeDTO;
import com.blog.api.models.entity.Like;
import com.blog.api.models.entity.User;

public class LikeMapper {
    public static LikeDTO toLikeDTO(Like like) {
        LikeDTO likeDTO = new LikeDTO();
        likeDTO.setLikeId(like.getLikeId());
        likeDTO.setId(like.getId());
        likeDTO.setArtId(ArticleMapper.toArticleDTO(like.getArtId()));
        likeDTO.setLikeTableType(like.getLikeTableType());
        likeDTO.setCreatedAt(like.getCreatedAt());
        likeDTO.setUpdatedAt(like.getUpdatedAt());
        for (User user : like.getLikedUsers()) {
            likeDTO.getLikedUsers().add(UserMapper.toUserDTO(user));
        }

        return likeDTO;
    }
}
