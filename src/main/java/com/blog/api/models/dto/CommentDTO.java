package com.blog.api.models.dto;

import com.blog.api.models.entity.Article;
import com.blog.api.models.entity.Comment;
import com.blog.api.models.entity.User;
import com.blog.api.types.TableType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentDTO {

    private Long id;
    private ArticleDTO artId;
    private UserDTO cmtUser;
    private Long parentId;
    private TableType tableType;
    private boolean isApproved;
    private String content;
    private boolean reply;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
