package com.blog.api.models.dto;

import com.blog.api.models.entity.Article;
import com.blog.api.models.entity.User;
import com.blog.api.types.NotificationType;
import com.blog.api.types.TableType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private Long id;
    private TableType type;
    private UserDTO sender;
    private NotificationType action;
    private ArticleDTO context;
    private UserDTO target;
    private String content;
    private boolean isRead;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
