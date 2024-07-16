package com.blog.api.entities;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "articles")
public class Article {


    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    String id;
    @ManyToOne()
    User author;
    String title;
    @Lob
    String content;
    String metaTitle;
    String description;
    String thumbnail = "";
    @ManyToMany()
    @JoinTable(
            name = "topics_of_article",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "topic_id")
    )
    Set<Topic> topics;
    @OneToMany(mappedBy = "commentableId",cascade = CascadeType.REMOVE)
    List<Comment> comments;

    @OneToMany(mappedBy = "reactionTableId",cascade = CascadeType.REMOVE)
    List<Reaction> reactions;

    @OneToMany(mappedBy = "bookmarkTableId",cascade = CascadeType.REMOVE)
    List<Bookmark> bookmarks;

    @Formula("(SELECT COUNT(*) FROM reactions r WHERE r.reaction_table_id = id)")
    Integer reactionCount = 0;
    @Formula("(SELECT COUNT(*) FROM comments c WHERE c.commentable_id = id)")
    Integer commentCount = 0;
    @Transient
    boolean isBookmarked;
    @Transient
    boolean isReacted;

    @Column(updatable = false)
    @CreationTimestamp
    Instant createdAt;
    @UpdateTimestamp
    Instant updatedAt;

}
