package com.blog.api.service;

import com.blog.api.models.dto.CommentDTO;
import com.blog.api.models.entity.Comment;
import com.blog.api.models.request.CommentRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
    public CommentDTO createComment(CommentRequest newComment);
    public List<CommentDTO> getAllCommentByArt(Long id);
    public List<CommentDTO> getAllCommentByParent(Long id);
    public List<CommentDTO> getCommentByCommentId(Long id);
    public List<CommentDTO> getAllComment();
    public boolean deleteCommentById(Long id);

}
