package com.blog.api.entities;

import com.blog.api.types.ReactionTableType;
import com.blog.api.types.TableType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;

//@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications")
//@EntityListeners(AuditingEntityListener.class)
public class Notification {
    @Id
    @SequenceGenerator(
            name="notification_sequence",
            sequenceName = "notification_sequence",
            allocationSize = 1
    )

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_sequence")
    private Long id;
    @Column
    private String notification_id = UUID.randomUUID().toString();
    @Column
    @Enumerated(EnumType.STRING)
    private TableType type;
    @ManyToOne()
    private User sender;
    @Column
    @Enumerated(EnumType.STRING)
    private ReactionTableType action;
    @ManyToOne()
    private Article context;
    @ManyToOne
    private User target;
    @Column
    private String content;
    @Column
    private boolean isRead;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    private LocalDateTime updatedAt;

}
