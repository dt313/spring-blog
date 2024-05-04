package com.blog.api.repository;

import com.blog.api.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, String> {
    public boolean existsByTitle(String title);

}
