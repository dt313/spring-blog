package com.blog.api.dto.response;

import com.blog.api.types.ReactionType;
import com.blog.api.types.TableType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReactionResponse {
    Long id;
    @JsonProperty("reaction_table_id")
    private Long reactionTableId;
    @Enumerated(EnumType.STRING)
    @JsonProperty("reaction_table_type")
    private TableType reactionTableType;
    @Enumerated(EnumType.STRING)
    private ReactionType type;
    @JsonProperty("reacted_user")
    private BasicUserResponse reactedUser;
    @JsonProperty("created_at")
    Instant createdAt;
}
