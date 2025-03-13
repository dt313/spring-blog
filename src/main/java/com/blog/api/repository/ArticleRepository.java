package com.blog.api.repository;

import com.blog.api.entities.Article;
import com.blog.api.entities.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    boolean existsByTitle(String title);
    Optional<Article> findBySlugAndIsPublished(String slug, boolean isPublished);

    List<Article> findByIsPublished(boolean isPublished);
    Optional<Article> findBySlug(String slug);
    List<Article> findAllByAuthor(User author);

    @Query("SELECT e FROM Article e WHERE e.isPublished = :isPublished AND (CAST(e.title AS text) ILIKE %:word% OR CAST(e.metaTitle AS text) ILIKE %:word% OR CAST(e.content AS text) ILIKE %:word%)")
    List<Article> findByTitleContainingAndIsPublished(

            @Param("word") String title, @Param("isPublished") boolean isPublished, PageRequest pageRequest);
    @Query("SELECT e FROM Article e WHERE e.isPublished = :isPublished AND (CAST(e.title AS text) ILIKE %:word% OR CAST(e.metaTitle AS text) ILIKE %:word% OR CAST(e.content AS text) ILIKE %:word%)")
    List<Article> findByTitleContainingAndIsPublished(
            @Param("word") String title,@Param("isPublished") boolean isPublished);
    @Query("SELECT a FROM Article a JOIN a.topics t WHERE t.name = :topic")
    List<Article> findByTopicName(@Param("topic") String topic,  PageRequest pageRequest);
    @Query("SELECT a FROM Article a JOIN a.topics t WHERE t.name = :topic")
    List<Article> findByTopicName(@Param("topic") String topic);

    @Query("SELECT a FROM Article a JOIN a.topics t WHERE t.name IN :topics OR a.author.id = :author" )
    List<Article> findByTopicsOrAuthor(
            @Param("topics") Set<String> topics , @Param("author") Long userId, PageRequest pageRequest);
}
