package com.blog.api.dto.response;

import com.blog.api.entities.Topic;
import com.blog.api.types.TableType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Formula;

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
public class QuestionResponse {
    String id;
    BasicUserResponse author;
    String content;
    Integer reactionCount;
    Integer commentCount;
    Set<Topic> topics = new HashSet<>();
    TableType tableType = TableType.QUESTION;
    @JsonProperty("is_bookmarked")
    boolean isBookmarked;
    @JsonProperty("is_reacted")
    boolean isReacted;
    Instant createdAt;
    Instant updatedAt;
}
