package com.blog.api.repository;

import com.blog.api.models.entity.Article;
import com.blog.api.models.entity.Comment;
import com.blog.api.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    public Comment findCommentByCmtUserAndArtIdAndParentId(User userId, Article artId, Long id);
    public List<Comment> findCommentByParentIdAndArtId( Long id, Article artId);
    public Comment findCommentByParentId( Long id);

}
