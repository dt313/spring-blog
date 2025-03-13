package com.blog.api.repository;

import com.blog.api.entities.Comment;
import com.blog.api.types.TableType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByCommentTypeAndCommentableId(TableType type, Long id, Pageable pageable);
    List<Comment> findAllByCommentTypeAndCommentableId(TableType type, Long id);

}
