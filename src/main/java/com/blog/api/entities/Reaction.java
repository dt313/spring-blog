package com.blog.api.entities;

import com.blog.api.types.ReactionTableType;
import com.blog.api.types.ReactionType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
    ReactionTableType reactionTableType;
    @ManyToOne
    User reactedUser;

//    @CreatedDate
//    private LocalDateTime createdAt;
//    @LastModifiedDate
//    private LocalDateTime updatedAt;



}
