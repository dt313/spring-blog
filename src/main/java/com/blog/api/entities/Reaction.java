package com.blog.api.entities;

import com.blog.api.types.ReactionType;
import com.blog.api.types.TableType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reactions")
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String reactionTableId;
    @Enumerated(EnumType.STRING)
    ReactionType type;
    @Enumerated(EnumType.STRING)
    TableType reactionTableType;
    @ManyToOne
    User reactedUser;
    @Column(updatable = false)
    @CreationTimestamp
    Instant createdAt;
}
