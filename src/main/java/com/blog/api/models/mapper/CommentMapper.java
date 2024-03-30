package com.blog.api.models.mapper;

import com.blog.api.models.dto.ArticleDTO;
import com.blog.api.models.dto.CommentDTO;
import com.blog.api.models.entity.Comment;

public class CommentMapper {
    public static CommentDTO toCommentDTO(Comment cmt) {
            CommentDTO temp = new CommentDTO();
            temp.setId(cmt.getId());
            temp.setContent(cmt.getContent());
            temp.setApproved(cmt.isApproved());
            temp.setArtId(ArticleMapper.toArticleDTO(cmt.getArtId()));
            temp.setCmtUser(UserMapper.toUserDTO(cmt.getCmtUser()));
            temp.setTableType(cmt.getTableType());
            temp.setParentId(cmt.getParentId());
            temp.setCreatedAt(cmt.getCreatedAt());
            temp.setUpdatedAt(cmt.getUpdatedAt());
            temp.setReply(!cmt.getReplies().isEmpty());
            return temp;
    }
}
