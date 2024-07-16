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
    @Mapping(source = "reacted", target = "isReacted")
    CommentResponse toCommentResponse(Comment comment);

}
