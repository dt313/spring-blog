package com.blog.api.models.request;

import com.blog.api.models.entity.Article;
import com.blog.api.models.entity.Comment;
import com.blog.api.models.entity.User;
import com.blog.api.types.TableType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentRequest {
    private Long artId;
    private Long cmtUser;
    private Long parentId;
    private TableType tableType;
    private String content;
}
