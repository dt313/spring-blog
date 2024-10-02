package com.blog.api.service;


import com.blog.api.dto.request.ArticleRequest;
import com.blog.api.dto.request.SuggestionRequest;
import com.blog.api.dto.response.ArticleResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ArticleService {
    List<ArticleResponse> getAll(String searchValue, int pageNumber, int pageSize);
    List<ArticleResponse> getAllFeaturedArticle(String searchValue, int pageNumber, int pageSize);
    List<ArticleResponse> getAllArticleByAuthor(Long id);
    List<ArticleResponse> getSuggestionsArticle(SuggestionRequest request);
    List<ArticleResponse> getAllByTopic(String name, int pageNumber, int pageSize);
    ArticleResponse getById(Long id);
    ArticleResponse getBySlug(String slug);
    ArticleResponse getBySlugWithAuth(String slug);

    ArticleResponse create(ArticleRequest insertArticle);
    ArticleResponse publish(Long id);
    ArticleResponse update(String slug, ArticleRequest updateArticle);
    void delete(Long id);
    Integer lengthOfArticleBySearchValue(String searchValue);
    Integer lengthOfArticleByTopic(String searchValue);
}
