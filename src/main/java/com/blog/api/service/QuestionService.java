package com.blog.api.service;


import com.blog.api.dto.request.ArticleRequest;
import com.blog.api.dto.request.QuestionRequest;
import com.blog.api.dto.request.SuggestionRequest;
import com.blog.api.dto.response.ArticleResponse;
import com.blog.api.dto.response.QuestionResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuestionService {
    public List<QuestionResponse> getAll(String searchValue , int pageNumber, int pageSize);
    public List<QuestionResponse> getAllByAuthor(String id);
    public List<QuestionResponse> getAllByTopic(String name,int pageNumber, int pageSize);
    public QuestionResponse getById(String id);
    public QuestionResponse create(QuestionRequest insertArticle);
    public QuestionResponse update(String id, QuestionRequest updateArticle);
    public void delete(String id);
    public Integer getLengthBySearchValue(String searchValue);
    public Integer getLengthByTopic(String topicName);
}
