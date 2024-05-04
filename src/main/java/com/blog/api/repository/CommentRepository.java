package com.blog.api.repository;

import com.blog.api.entities.Comment;
import com.blog.api.types.TableType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    List<Comment> findAllByCommentTypeAndArtId(TableType type, String id, Pageable pageable);
    List<Comment> findAllByCommentTypeAndArtIdAndParentId(
            TableType type, String artId, String parentId, Pageable pageable);
}
