package com.blog.api.repository;

import com.blog.api.models.entity.Article;
import com.blog.api.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ArticleRepository extends JpaRepository<Article, Long> {
    public List<Article> findArticleByTitle(String title);

}
