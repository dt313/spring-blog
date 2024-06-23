package com.blog.api.entities;

import com.blog.api.types.ReactionType;
import com.blog.api.types.TableType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "bookmarks")
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String bookmarkTableId;
    @Enumerated(EnumType.STRING)
    TableType bookmarkTableType;
    @ManyToOne
    User bookmarkedUser;
    @Column(updatable = false)
    @CreationTimestamp
    Instant createdAt;
    @UpdateTimestamp
    Instant updatedAt;
}
