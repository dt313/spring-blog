package com.blog.api.service;

import com.blog.api.dto.request.CommentCreationRequest;
import com.blog.api.dto.response.CommentResponse;
import com.blog.api.types.TableType;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface CommentService {
    public CommentResponse create(CommentCreationRequest request);
    public List<CommentResponse> getCommentByTypeAndArtId(TableType type, String id, int pNumber, int pSize);
    public List<CommentResponse> getAllComment();
    public CommentResponse getCommentById(String id);

    public List<CommentResponse> getCommentsByArtAndParentId(TableType type, String artId, String parentId,
                                                             int pNumber, int pSize);
    public void delete(String id);

}
