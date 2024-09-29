package com.blog.api.entities;

import com.blog.api.types.NotificationType;
import com.blog.api.types.TableType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_seq")
    @SequenceGenerator(name = "notification_seq", sequenceName = "notification_seq", allocationSize = 1)
    Long id;
    @Enumerated(EnumType.STRING)
    NotificationType type;

    @ManyToOne
    User sender;
    @ManyToOne
    User receiver;

    TableType contextType;
    Long contextId;

    TableType directObjectType;
    Long directObjectId;

    @JsonProperty("is_readed")
    @Value("false")
    boolean isReaded;


    @Column(updatable = false)
    @CreationTimestamp
    Instant createdAt;
    @UpdateTimestamp
    Instant updatedAt;

}
