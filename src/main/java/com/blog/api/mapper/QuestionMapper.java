package com.blog.api.mapper;

import com.blog.api.dto.request.ArticleRequest;
import com.blog.api.dto.request.QuestionRequest;
import com.blog.api.dto.response.ArticleResponse;
import com.blog.api.dto.response.QuestionResponse;
import com.blog.api.entities.Article;
import com.blog.api.entities.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "topics", ignore = true)
    Question toQuestion(QuestionRequest request);
    @Mapping(target = "author", ignore = true)
    QuestionResponse toQuestionResponse(Question article);
    @Mapping(target = "topics", ignore = true)
    @Mapping(target = "author" , ignore = true)
    void updateQuestion(@MappingTarget Question article,  QuestionRequest request);

    }
