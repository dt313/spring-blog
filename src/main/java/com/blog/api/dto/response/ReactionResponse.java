package com.blog.api.dto.response;

import com.blog.api.types.ReactionType;
import com.blog.api.types.TableType;
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
    private String reactionTableId;
    @Enumerated(EnumType.STRING)
    private TableType reactionTableType;
    @Enumerated(EnumType.STRING)
    private ReactionType type;
    private BasicUserResponse reactedUser;
    Instant createdAt;
}
