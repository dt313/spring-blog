package com.blog.api.service;


import com.blog.api.models.dto.ArticleDTO;
import com.blog.api.models.entity.Article;
import com.blog.api.models.request.ArticleRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ArticleService {
    public List<ArticleDTO> getAllArticle();
    public ArticleDTO getArticleById(Long id);
    public ArticleDTO insertArticle(ArticleRequest insertArticle);
    public ArticleDTO updateArticle(Long id, ArticleRequest updateArticle);
    public boolean deleteArticle(Long id);


}
