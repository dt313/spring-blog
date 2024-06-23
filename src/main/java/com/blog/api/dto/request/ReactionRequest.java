package com.blog.api.dto.request;

import com.blog.api.types.ReactionType;
import com.blog.api.types.TableType;
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
    private TableType reactionTableType;
    @Enumerated(EnumType.STRING)
    private ReactionType type;
    private String reactedUser;
}
