package com.blog.api.mapper;

import com.blog.api.dto.request.NotificationRequest;
import com.blog.api.dto.response.NotificationResponse;
import com.blog.api.entities.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mapping(target="sender", ignore = true)
    @Mapping(target="receiver", ignore = true)
    Notification toNotification(NotificationRequest notificationRequest);

    @Mapping(target="sender", ignore = true)
    @Mapping(target="receiver", ignore = true)
    @Mapping(source = "readed", target = "isReaded")
    NotificationResponse toNotificationResponse(Notification notification);

}
