package com.blog.api.models.mapper;

import com.blog.api.models.dto.ArticleDTO;
import com.blog.api.models.entity.Article;

public class ArticleMapper {
    public static ArticleDTO toArticleDTO(Article art) {
        ArticleDTO temp = new ArticleDTO();
        temp.setId(art.getId());
        temp.setTitle(art.getTitle());
        temp.setContent(art.getContent());
        temp.setCommentCount(art.getCommentCount());
        temp.setLikeCount(art.getLikeCount());
        temp.setAuthor(UserMapper.toUserDTO(art.getAuthor()));
        temp.setMetadata(art.getMetadata());
        temp.setTopics(TopicMapper.toListTopicDTO(art.getTopics()));
        temp.setCreatedAt(art.getCreatedAt());
        temp.setUpdatedAt(art.getUpdatedAt());
        return temp;
    }
}
