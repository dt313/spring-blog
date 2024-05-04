package com.blog.api.service;


import com.blog.api.dto.response.ArticleResponse;
import com.blog.api.dto.request.ArticleRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ArticleService {
    public List<ArticleResponse> getAll();
    public ArticleResponse getById(String id);
    public ArticleResponse create(ArticleRequest insertArticle);
    public ArticleResponse update(String id, ArticleRequest updateArticle);
    public void delete(String id);


}
