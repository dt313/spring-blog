package com.blog.api.dto.request;

import com.blog.api.types.NotificationType;
import com.blog.api.types.TableType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationRequest {
    @Enumerated(EnumType.STRING)
    String sender;
    NotificationType type;
    String receiver;
    TableType contextType;
    String contextId;
    TableType directObjectType;
    String directObjectId;
}
