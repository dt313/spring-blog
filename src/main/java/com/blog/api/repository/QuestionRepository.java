package com.blog.api.repository;

import com.blog.api.entities.Article;
import com.blog.api.entities.Question;
import com.blog.api.entities.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface QuestionRepository extends JpaRepository<Question, String> {
    public List<Question> findAllByAuthor(User author);
    @Query("SELECT e FROM Question e WHERE e.content LIKE %:word%")
    List<Question> findByContentContaining(@Param("word")String content, PageRequest pageRequest);
    @Query("SELECT e FROM Question e WHERE e.content LIKE %:word%")

    List<Question> findByContentContaining(@Param("word") String content);
    @Query("SELECT a FROM Question a JOIN a.topics t WHERE t.name = :topic")
    List<Question> findByTopicName(@Param("topic") String topic,  PageRequest pageRequest);
    @Query("SELECT a FROM Question a JOIN a.topics t WHERE t.name = :topic")
    List<Question> findByTopicName(@Param("topic") String topic);
//
//    @Query("SELECT a FROM Question a JOIN a.topics t WHERE t.name IN :topics OR a.author.id = :author" )
//    List<Question> findByTopicsOrAuthor(
//            @Param("topics") Set<String> topics , @Param("author") String userId, PageRequest pageRequest);
}
