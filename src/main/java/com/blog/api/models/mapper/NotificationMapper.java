package com.blog.api.models.mapper;

import com.blog.api.models.dto.NotificationDTO;
import com.blog.api.models.entity.Notification;

public class NotificationMapper {
    public static NotificationDTO toNotificationDTO(Notification notification) {
        NotificationDTO temp = new NotificationDTO();

        temp.setId(notification.getId());
        temp.setSender(UserMapper.toUserDTO(notification.getSender()));
        temp.setTarget(UserMapper.toUserDTO(notification.getTarget()));
        temp.setContent(notification.getContent());
        temp.setContext(ArticleMapper.toArticleDTO(notification.getContext()));
        temp.setAction(notification.getAction());
        temp.setRead(notification.isRead());
        temp.setType(notification.getType());
        temp.setCreatedAt(notification.getCreatedAt());
        temp.setUpdatedAt(notification.getUpdatedAt());
        return temp;
    }
}
