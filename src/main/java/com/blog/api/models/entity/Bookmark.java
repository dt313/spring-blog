package com.blog.api.models.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "bookmarks")
@EntityListeners(AuditingEntityListener.class)
public class Bookmark {
    @Id
    @SequenceGenerator(name="bookmark_sequence", sequenceName ="bookmark_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookmark_sequence")
    private Long id;
    @Column
    private String bookmarkId = UUID.randomUUID().toString();
    @ManyToOne
    @JoinColumn(name = "art_id")
    private Article artId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;
    @Column
    private boolean isBookmarked;


    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public Bookmark(Article artId, User userId, boolean isBookmarked) {
        this.artId = artId;
        this.userId = userId;
        this.isBookmarked = isBookmarked;
    }
}
