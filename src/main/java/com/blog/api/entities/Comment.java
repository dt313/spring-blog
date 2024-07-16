package com.blog.api.entities;

import com.blog.api.types.TableType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
import java.util.ArrayList;
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

    @OneToMany(mappedBy = "reactionTableId",cascade = CascadeType.REMOVE)
    List<Reaction> reactions;

    @Formula("COALESCE((SELECT COUNT(*) FROM reactions r WHERE r.reaction_table_id = id), 0)")
    private Integer reactionCount;
    @Formula("COALESCE((SELECT COUNT(*) FROM comments r WHERE r.commentable_id = id), 0)")
    Integer repliesCount = 0;

    @Transient
    boolean isReacted = false;
    @Transient
    boolean isApproved = false;

    @Transient
    @JsonProperty("replies")
    List<Object> replies = new ArrayList<>();

    @Column(updatable = false)
    @CreationTimestamp
    Instant createdAt;
    @UpdateTimestamp
    Instant updatedAt;

}
