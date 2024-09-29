package com.blog.api.dto.response;

import com.blog.api.entities.Topic;
import com.blog.api.types.ReactionType;
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
    Long id;
    BasicUserResponse author;
    String title;
    String slug;
    String content;
    String thumbnail;
    @JsonProperty("meta_title")
    String metaTitle;
    String description;
    @JsonProperty("reaction_count")
    Integer reactionCount;
    @JsonProperty("reacted_type")
    ReactionType reactedType;
    @JsonProperty("comment_count")
    Integer commentCount;
    @JsonProperty("table_type")
    TableType tableType = TableType.ARTICLE;
    @JsonProperty("is_bookmarked")
    boolean isBookmarked;
    @JsonProperty("is_reacted")
    boolean isReacted;
    Set<Topic> topics = new HashSet<>();

    @JsonProperty("reactions")
    Set<ReactionResponse> reactions;

    @JsonProperty("created_at")
    Instant createdAt;
    @JsonProperty("updated_at")
    Instant updatedAt;
}
