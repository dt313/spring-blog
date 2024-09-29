package com.blog.api.service;

import com.blog.api.dto.request.NotificationRequest;
import com.blog.api.dto.response.NotificationResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotificationService {
    List<NotificationResponse> getAll(int pageSize, int pageNumber);
    List<NotificationResponse> getAllByUser(Long id, int pageNumber, int pageSize);

    NotificationResponse create(NotificationRequest notificationRequest);
    boolean read(Long id);
    boolean readAll();

}
