package com.blog.api.service;

import com.blog.api.dto.request.CommentCreationRequest;
import com.blog.api.dto.response.CommentResponse;
import com.blog.api.types.TableType;
import org.hibernate.Internal;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface CommentService {
    public CommentResponse create(CommentCreationRequest request);
    public List<CommentResponse> getByTypeAndCommentableId(TableType type, String id, int pNumber, int pSize);
    public List<CommentResponse> getAllComment();
    public CommentResponse getById(String id);

    public void delete(String id);

    public Integer getLengthByCommentTypeAndCommentableId(TableType type, String id);

}
