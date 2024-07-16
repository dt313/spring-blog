package com.blog.api.dto.response;

import com.blog.api.entities.User;
import com.blog.api.types.TableType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookmarkResponse {
    String id;
    String bookmarkTableId;
    @Enumerated(EnumType.STRING)
    TableType bookmarkTableType;
    @ManyToOne
    BasicUserResponse bookmarkedUser;
    Object content;
    Instant createdAt;
    Instant updatedAt;
}
