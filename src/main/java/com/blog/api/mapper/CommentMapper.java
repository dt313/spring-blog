package com.blog.api.mapper;

import com.blog.api.dto.request.CommentCreationRequest;
import com.blog.api.dto.response.CommentResponse;
import com.blog.api.entities.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "publisher", ignore = true)
    Comment toComment(CommentCreationRequest request);
    @Mapping(target = "publisher", ignore = true)
    @Mapping(target = "isReply", source="reply")
    @Mapping(target = "isApproved", source="approved")
    CommentResponse toCommentResponse(Comment comment);

}
