package com.blog.api.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ArticleRequest {
    String author;
    String title;
    String metaTitle;
    String content;
    String thumbnail;
    String description;
    Set<String> topics;
}
