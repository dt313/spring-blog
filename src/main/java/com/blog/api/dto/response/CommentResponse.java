package com.blog.api.dto.response;

import com.blog.api.types.TableType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {
    String id;
    String artId;
    String parentId;
    BasicUserResponse publisher;
    @Enumerated(EnumType.STRING)
    TableType commentType;
    String content;
    boolean isReply;
    boolean isApproved;
}
