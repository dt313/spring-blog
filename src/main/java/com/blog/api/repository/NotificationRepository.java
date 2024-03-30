package com.blog.api.repository;

import com.blog.api.models.entity.Notification;
import com.blog.api.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface NotificationRepository extends JpaRepository<Notification, Long> {
    public List<Notification> findNotificationByTarget(User user);
    public List<Notification> findNotificationBySender(User user);
    public List<Notification> findNotificationByTargetAndIsRead(User user, boolean isRead);
}
