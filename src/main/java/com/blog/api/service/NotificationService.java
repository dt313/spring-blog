package com.blog.api.service;

import com.blog.api.dto.request.NotificationRequest;
import com.blog.api.dto.response.NotificationResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotificationService {
    public List<NotificationResponse> getAll(int pageSize,int pageNumber);
    public List<NotificationResponse> getAllByUser (String id , int pageNumber, int pageSize);

    public NotificationResponse create(NotificationRequest notificationRequest);
    public boolean read(String id);
    public boolean readAll();

}
