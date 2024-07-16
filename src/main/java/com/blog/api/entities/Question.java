package com.blog.api.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "questions")
@EntityListeners(AuditingEntityListener.class)
public class Question {


    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    String id;
    @ManyToOne()
    User author;
    @Lob
    String content;
    @OneToMany(mappedBy = "commentableId",cascade = CascadeType.REMOVE)
    List<Comment> comments;

    @OneToMany(mappedBy = "reactionTableId",cascade = CascadeType.REMOVE)
    List<Reaction> reactions;
    @ManyToMany()
    @JoinTable(
            name = "topics_of_question",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "topic_id")
    )
    Set<Topic> topics;
    @OneToMany(mappedBy = "bookmarkTableId",cascade = CascadeType.REMOVE)
    List<Bookmark> bookmarks;

    @Formula("(SELECT COUNT(*) FROM reactions r WHERE r.reaction_table_id = id)")
    Integer reactionCount = 0;
    @Formula("(SELECT COUNT(*) FROM comments c WHERE c.commentable_id = id)")
    Integer commentCount = 0;

    @Transient
    boolean isBookmarked = false;
    @Transient
    boolean isReacted = false;
    @Column(updatable = false)
    @CreationTimestamp
    Instant createdAt;
    @UpdateTimestamp
    Instant updatedAt;



}
