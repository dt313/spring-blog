package com.blog.api.service;


import com.blog.api.dto.request.SuggestionRequest;
import com.blog.api.dto.response.ArticleResponse;
import com.blog.api.dto.request.ArticleRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface ArticleService {
    public List<ArticleResponse> getAll(String searchValue ,int pageNumber, int pageSize);
    public List<ArticleResponse> getAllArticleByAuthor(String id);
    public List<ArticleResponse> getSuggestionsArticle(SuggestionRequest request);
    public List<ArticleResponse> getAllByTopic(String name,int pageNumber, int pageSize);
    public ArticleResponse getById(String id);
    public ArticleResponse create(ArticleRequest insertArticle);
    public ArticleResponse update(String id, ArticleRequest updateArticle);
    public void delete(String id);
    public Integer lengthOfArticleBySearchValue(String searchValue);
    public Integer lengthOfArticleByTopic(String searchValue);
}
