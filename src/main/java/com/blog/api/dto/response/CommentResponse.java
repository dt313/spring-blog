package com.blog.api.dto.response;

import com.blog.api.types.TableType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.time.LocalDateTime;

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
    boolean isApproved;
    Instant createdAt;
    Instant updatedAt;
}
