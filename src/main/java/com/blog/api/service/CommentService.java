package com.blog.api.service;

import com.blog.api.dto.request.CommentCreationRequest;
import com.blog.api.dto.response.CommentResponse;
import com.blog.api.types.TableType;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface CommentService {
    CommentResponse create(CommentCreationRequest request);
    List<CommentResponse> getByTypeAndCommentableId(TableType type, Long id, int pNumber, int pSize);
    List<CommentResponse> getAllComment();
    CommentResponse getById(Long id);

    void delete(Long id);

    Integer getLengthByCommentTypeAndCommentableId(TableType type, Long id);

}
