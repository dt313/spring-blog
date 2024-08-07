package com.blog.api.entities;

import com.blog.api.types.NotificationType;
import com.blog.api.types.TableType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Enumerated(EnumType.STRING)
    NotificationType type;

    @ManyToOne
    User sender;
    @ManyToOne
    User receiver;

    TableType contextType;
    String contextId;

    TableType directObjectType;
    String directObjectId;
    @JsonProperty("is_readed")
    boolean isReaded;


    @Column(updatable = false)
    @CreationTimestamp
    Instant createdAt;
    @UpdateTimestamp
    Instant updatedAt;

}
