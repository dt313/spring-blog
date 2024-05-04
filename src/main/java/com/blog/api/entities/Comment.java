package com.blog.api.entities;

import com.blog.api.types.TableType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
    String artId;
    String parentId;
    @Enumerated(EnumType.STRING)
    TableType commentType;
    @ManyToOne
    User publisher;
    String content;
    boolean isReply;
    boolean isApproved;

    @OneToMany(mappedBy = "reactionTableId",cascade = CascadeType.REMOVE)
    List<Reaction> reactions;


//    @CreatedDate
//    @Column(updatable = false)
//    private LocalDateTime createdAt;
//    @LastModifiedDate
//    private LocalDateTime updatedAt;

}
