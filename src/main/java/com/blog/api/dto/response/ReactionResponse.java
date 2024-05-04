package com.blog.api.dto.response;

import com.blog.api.types.ReactionTableType;
import com.blog.api.types.ReactionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReactionResponse {
    private String reactionTableId;
    @Enumerated(EnumType.STRING)
    private ReactionTableType reactionTableType;
    @Enumerated(EnumType.STRING)
    private ReactionType type;
    private BasicUserResponse reactedUser;
}
