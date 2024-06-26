package com.blog.api.dto.request;

import com.blog.api.types.TableType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentCreationRequest {
    String commentableId;
    String publisher;
    TableType commentType;
    String content;
}
