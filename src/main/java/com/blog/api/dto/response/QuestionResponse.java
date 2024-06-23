package com.blog.api.dto.response;

import com.blog.api.entities.Topic;
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
public class QuestionResponse {
    String id;
    BasicUserResponse author;
    String content;
    Integer reactionCount;
    Integer commentCount;
    Set<Topic> topics = new HashSet<>();
    Instant createdAt;
    Instant updatedAt;
}
