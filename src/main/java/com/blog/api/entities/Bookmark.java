package com.blog.api.entities;

import com.blog.api.types.ReactionTableType;
import com.blog.api.types.ReactionType;
import com.blog.api.types.TableType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String bookmarkTableId;
    @Enumerated(EnumType.STRING)
    TableType bookmarkTableType;
    @ManyToOne
    User bookmarkedUser;
}
