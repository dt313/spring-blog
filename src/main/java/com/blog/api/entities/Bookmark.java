package com.blog.api.entities;

import com.blog.api.types.TableType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "bookmarks")
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookmark_id")
    @SequenceGenerator(name = "bookmark_id", sequenceName = "bookmark_id", allocationSize = 1)
    Long id;
    Long bookmarkTableId;
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
