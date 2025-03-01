package com.blog.api.entities;

import com.blog.api.types.TableType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
//@EntityListeners(AuditingEntityListener.class)
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_seq")
    @SequenceGenerator(name = "comment_seq", sequenceName = "comment_seq", allocationSize = 1)
    Long id;
    Long commentableId;
    @Enumerated(EnumType.STRING)
    TableType commentType;
    @ManyToOne
    User publisher;

    String content;

    @OneToMany(mappedBy = "reactionTableId",cascade = CascadeType.REMOVE)
    List<Reaction> reactions;

    @Formula("COALESCE((SELECT COUNT(*) FROM reactions r WHERE r.reaction_table_id = id), 0)")
    Integer reactionCount;
    @Formula("COALESCE((SELECT COUNT(*) FROM comments c WHERE c.commentable_id = id and c.comment_type = 'COMMENT'), 0)")
    Integer repliesCount = 0;

    @Transient
    boolean isReacted = false;
    @Transient
    boolean isApproved = false;


    @OneToMany(mappedBy = "commentableId", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    List<Comment> replies = new ArrayList<>();


    @JsonProperty("replies")
    public List<Comment> getReplies() {
        return new ArrayList<>();
    }

    @Column(updatable = false)
    @CreationTimestamp
    Instant createdAt;
    @UpdateTimestamp
    Instant updatedAt;

    @OneToMany(mappedBy = "directObjectId", cascade = CascadeType.REMOVE)
    List<Notification> notifications;

}
