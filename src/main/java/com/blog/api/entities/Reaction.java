package com.blog.api.entities;

import com.blog.api.types.ReactionType;
import com.blog.api.types.TableType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reactions")
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reaction_seq")
    @SequenceGenerator(name = "reaction_seq", sequenceName = "reaction_seq", allocationSize = 1)
    Long id;
    Long reactionTableId;
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
