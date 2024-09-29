package com.blog.api.dto.response;

import com.blog.api.types.TableType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookmarkResponse {
    Long id;
    @JsonProperty("bookmark_table_id")
    Long bookmarkTableId;
    @Enumerated(EnumType.STRING)
    @JsonProperty("bookmark_table_type")
    TableType bookmarkTableType;
    @ManyToOne
    @JsonProperty("bookmark_table_user")
    BasicUserResponse bookmarkedUser;
    Object content;
    @JsonProperty("created_at")
    Instant createdAt;
    @JsonProperty("updated_at")
    Instant updatedAt;
}
