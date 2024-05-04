package com.blog.api.dto.response;

import com.blog.api.entities.Topic;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArticleResponse {
    String id;
    BasicUserResponse author;
    String title;
    String content;
    String thumbnail;
    String metaTitle;
    String description;
    Set<Topic> topics = new HashSet<>();
}
