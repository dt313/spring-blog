package com.blog.api.dto.response;

import com.blog.api.entities.Comment;
import com.blog.api.types.TableType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Transient;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Array;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {
    String id;
    String commentableId;
    BasicUserResponse publisher;
    @Enumerated(EnumType.STRING)
    TableType commentType;
    String content;
    Integer repliesCount;
    Integer reactionCount;
    @JsonProperty("replies")
    @JsonInclude(JsonInclude.Include.ALWAYS)
    List<Object> replies = new ArrayList<>();
    @JsonProperty("is_reacted")
    boolean isReacted;
    @JsonProperty("is_approved")
    boolean isApproved;
    Instant createdAt;
    Instant updatedAt;

}
