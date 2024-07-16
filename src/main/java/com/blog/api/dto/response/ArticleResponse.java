package com.blog.api.dto.response;

import com.blog.api.entities.Topic;
import com.blog.api.types.TableType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleResponse {
    String id;
    BasicUserResponse author;
    String title;
    String content;
    String thumbnail;
    String metaTitle;
    String description;
    Integer reactionCount;
    Integer commentCount;
    TableType tableType = TableType.ARTICLE;
    @JsonProperty("is_bookmarked")
    boolean isBookmarked;
    @JsonProperty("is_reacted")
    boolean isReacted;
    Set<Topic> topics = new HashSet<>();
    Instant createdAt;
    Instant updatedAt;
}
