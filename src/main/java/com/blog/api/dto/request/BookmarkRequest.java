package com.blog.api.dto.request;

import com.blog.api.types.TableType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookmarkRequest {
    Long bookmarkTableId;
    @Enumerated(EnumType.STRING)
    TableType bookmarkTableType;
    @ManyToOne
    Long bookmarkedUser;
}
