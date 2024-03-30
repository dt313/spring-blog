package com.blog.api.models.request;

import com.blog.api.models.dto.TopicDTO;
import com.blog.api.models.entity.Metadata;
import com.blog.api.models.entity.Topic;
import com.blog.api.models.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ArticleRequest {
    private Long id;
    private Long author;
    private String title;
    private String content;
    private String thumbnail;
    private Metadata metadata;
    private Set<String> topics = new HashSet<>();
}
