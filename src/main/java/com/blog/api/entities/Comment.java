package com.blog.api.entities;

import com.blog.api.types.TableType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String commentableId;
    @Enumerated(EnumType.STRING)
    TableType commentType;
    @ManyToOne
    User publisher;
    String content;
    boolean isApproved;

    @OneToMany(mappedBy = "reactionTableId",cascade = CascadeType.REMOVE)
    List<Reaction> reactions;

    @Formula("(SELECT COUNT(*) FROM reactions r WHERE r.reaction_table_id = id)")
    Integer reactionCount;
    @Formula("(SELECT COUNT(*) FROM comments c WHERE c.commentable_id = id)")
    Integer repliesCount;



    @Column(updatable = false)
    @CreationTimestamp
    Instant createdAt;
    @UpdateTimestamp
    Instant updatedAt;

}
