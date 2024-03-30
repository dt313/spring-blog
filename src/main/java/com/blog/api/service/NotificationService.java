package com.blog.api.service;

import com.blog.api.models.dto.NotificationDTO;
import com.blog.api.models.request.NotificationRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotificationService {
    public List<NotificationDTO> getNotificationByTarget(Long id);
    public List<NotificationDTO> getNotificationBySender(Long id);
    public NotificationDTO getNotificationById(Long id);
    public List<NotificationDTO> getALlNotifications();
    public NotificationDTO createNotification(NotificationRequest notificationRequest);
    public NotificationDTO readNotificationById(Long id);
    public List<NotificationDTO> readAllNotificationByTarget(Long id);
    public boolean deleteNotificationById(Long id);

 }
