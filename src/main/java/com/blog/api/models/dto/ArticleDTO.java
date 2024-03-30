package com.blog.api.models.dto;

import com.blog.api.models.entity.Metadata;
import com.blog.api.models.entity.Topic;
import com.blog.api.models.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.repository.Meta;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ArticleDTO {

    private Long id;
    private UserDTO author;
    private String title;
    private String content;
    private String thumbnail;
    private int likeCount;
    private Metadata metadata;
    private Set<TopicDTO> topics = new HashSet<>();
    private int commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
