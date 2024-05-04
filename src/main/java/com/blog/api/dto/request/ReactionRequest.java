package com.blog.api.dto.request;

import com.blog.api.entities.Article;
import com.blog.api.entities.User;
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
public class ReactionRequest {
    private String reactionTableId;
    @Enumerated(EnumType.STRING)
    private ReactionTableType reactionTableType;
    @Enumerated(EnumType.STRING)
    private ReactionType type;
    private String reactedUser;
}
