package com.blog.api.models.dto;

import com.blog.api.models.entity.Article;
import com.blog.api.models.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkDTO {
    private Long id;
    private String bookmarkId;
    private ArticleDTO artId;
    private UserDTO userId;
    private LocalDateTime createdAt;
}
