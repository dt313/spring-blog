package com.blog.api.dto.response;

import com.blog.api.types.NotificationType;
import com.blog.api.types.TableType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationResponse {
    Long id;
    @Enumerated(EnumType.STRING)
    NotificationType type;
    BasicUserResponse sender;
    BasicUserResponse receiver;
    @JsonProperty("context_type")
    TableType contextType;
    @JsonProperty("context_id")
    Long contextId;
    Object context;
    @JsonProperty("direct_object_type")
    TableType directObjectType;
    @JsonProperty("direct_object_id")
    Long directObjectId;
    @JsonProperty("direct_object")
    Object directObject;
    @JsonProperty("is_readed")
    boolean isReaded;

    @JsonProperty("created_at")
    Instant createdAt;
    @JsonProperty("updated_at")
    Instant updatedAt;
}
