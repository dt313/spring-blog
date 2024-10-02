package com.blog.api.entities;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "articles")
public class Article {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "article_seq")
    @SequenceGenerator(name = "article_seq", sequenceName = "article_seq", allocationSize = 1)
    Long id;
    @ManyToOne()
    User author;
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Title cannot empty")
    @Size(min = 25, max = 200, message = "Title length must be between 25 and 200 characters")
    String title;
    @Column(nullable = false, unique = true)
    String slug;
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Content cannot empty")
    @Size(min = 100, message = "Content length must be larger 100 characters")
    @Lob
    String content;
    @Size(max = 100, message = "Meta title length must be smaller 100 characters")
    String metaTitle;
    @Size(max = 160, message = "Meta description length must be smaller 160 characters")
    String description;
    String thumbnail = "";
    @ManyToMany(fetch = FetchType.EAGER)
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

    @Formula("(SELECT COUNT(*) FROM comments c WHERE c.commentable_id = id and c.comment_type = 'ARTICLE')")
    Integer commentCount = 0;
    @Transient
    boolean isBookmarked;
    @Transient
    boolean isReacted;

    boolean isPublished;

    Instant publishAt;
    @Column(updatable = false)
    @CreationTimestamp
    Instant createdAt;
    @UpdateTimestamp
    Instant updatedAt;

}
