package com.blog.api.dto.response;

import com.blog.api.types.ReactionType;
import com.blog.api.types.TableType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {
    Long id;
    @JsonProperty("comment_table_id")
    Long commentableId;
    BasicUserResponse publisher;
    @Enumerated(EnumType.STRING)
    @JsonProperty("comment_type")
    TableType commentType;
    String content;
    @JsonProperty("replies_count")
    Integer repliesCount;
    @JsonProperty("reaction_count")
    Integer reactionCount;
    @JsonProperty("reacted_type")
    ReactionType reactedType;
    @JsonProperty("replies")
    @JsonInclude(JsonInclude.Include.ALWAYS)
    List<Object> replies = new ArrayList<>();
    @JsonProperty("is_reacted")
    boolean isReacted;
    @JsonProperty("is_approved")
    boolean isApproved;
    Set<ReactionResponse> reactions = new HashSet<>();;
    @JsonProperty("created_at")
    Instant createdAt;
    @JsonProperty("updated_at")
    Instant updatedAt;

}
