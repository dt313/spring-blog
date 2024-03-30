package com.blog.api.models.entity;

import com.blog.api.types.NotificationType;
import com.blog.api.types.TableType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications")
@EntityListeners(AuditingEntityListener.class)
public class Notification {
    @Id
    @SequenceGenerator(
            name="noti_sequence",
            sequenceName = "noti_sequence",
            allocationSize = 1
    )

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "noti_sequence")
    private Long id;
    @Column
    private String notificaitonId = UUID.randomUUID().toString();
    @Column
    @Enumerated(EnumType.STRING)
    private TableType type;
    @ManyToOne()
    private User sender;
    @Column
    @Enumerated(EnumType.STRING)
    private NotificationType action;
    @ManyToOne
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

    public Notification(TableType type, NotificationType action, User sender, Article context, User target) {
        this.type = type;
        this.sender = sender;
        this.action = action;
        this.context = context;
        this.target = target;
    }
}
