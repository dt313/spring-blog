package com.blog.api.models.request;

import com.blog.api.types.NotificationType;
import com.blog.api.types.TableType;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NotificationRequest {
    private Long sender;
    private NotificationType action;
    private Long context;
    private Long target;
    private TableType type;

}
