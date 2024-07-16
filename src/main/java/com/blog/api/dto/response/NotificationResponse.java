package com.blog.api.dto.response;

import com.blog.api.types.NotificationType;
import com.blog.api.types.TableType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationResponse {
    String id;
    @Enumerated(EnumType.STRING)
    NotificationType type;
    BasicUserResponse sender;
    BasicUserResponse receiver;
    TableType contextType;
    String contextId;
    Object context;
    TableType directObjectType;
    String directObjectId;
    Object directObject;
    @JsonProperty("is_readed")
    boolean isReaded;

    Instant createdAt;
    Instant updatedAt;
}
