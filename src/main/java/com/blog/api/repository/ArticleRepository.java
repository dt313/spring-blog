package com.blog.api.repository;

import com.blog.api.entities.Article;
import com.blog.api.entities.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ArticleRepository extends JpaRepository<Article, String> {
    boolean existsByTitle(String title);
    List<Article> findAllByAuthor(User author);
    @Query("SELECT e FROM Article e WHERE e.title LIKE %:word% OR e.metaTitle LIKE %:word% OR e.content LIKE %:word%"  )
    List<Article> findByTitleContaining(@Param("word")String title, PageRequest pageRequest);
    @Query("SELECT e FROM Article e WHERE e.title LIKE %:word% OR e.metaTitle LIKE %:word% OR e.content LIKE %:word%"  )

    List<Article> findByTitleContaining(@Param("word") String title);
    @Query("SELECT a FROM Article a JOIN a.topics t WHERE t.name = :topic")
    List<Article> findByTopicName(@Param("topic") String topic,  PageRequest pageRequest);
    @Query("SELECT a FROM Article a JOIN a.topics t WHERE t.name = :topic")
    List<Article> findByTopicName(@Param("topic") String topic);

    @Query("SELECT a FROM Article a JOIN a.topics t WHERE t.name IN :topics OR a.author.id = :author" )
    List<Article> findByTopicsOrAuthor(
            @Param("topics") Set<String> topics , @Param("author") String userId, PageRequest pageRequest);
}
