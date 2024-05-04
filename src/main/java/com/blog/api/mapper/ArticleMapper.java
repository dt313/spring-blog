package com.blog.api.mapper;

import com.blog.api.dto.request.ArticleRequest;
import com.blog.api.dto.response.ArticleResponse;
import com.blog.api.entities.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ArticleMapper {
    @Mapping(target = "topics", ignore = true)
    @Mapping(target = "author", ignore = true)
    Article toArticle(ArticleRequest request);
    @Mapping(target = "author", ignore = true)
    ArticleResponse toArticleResponse(Article article);
    @Mapping(target = "author" , ignore = true)
    @Mapping(target = "topics" , ignore = true)
    void updateArticle(@MappingTarget Article article,  ArticleRequest request);

    }
