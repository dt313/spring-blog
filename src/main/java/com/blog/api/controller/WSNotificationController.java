package com.blog.api.controller;

import com.blog.api.dto.request.Message;
import com.blog.api.dto.request.NotificationRequest;
import com.blog.api.dto.request.RoleRequest;
import com.blog.api.dto.response.NotificationResponse;
import com.blog.api.dto.response.RoleResponse;
import com.blog.api.mapper.RoleMapper;
import com.blog.api.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
public class WSNotificationController {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    NotificationService notificationService;
    @MessageMapping("/ping")
    @SendTo("/ws/pong")
    private String ping(String message) throws Exception {
        System.out.println("WS received : " + message );
        return "PONG";
    }

    @MessageMapping("/notification")
    private void sendNotification(@Payload NotificationRequest notificationRequest) throws Exception
    {
        System.out.println(notificationRequest);

        if(!notificationRequest.getSender().equals(notificationRequest.getReceiver())) {
            NotificationResponse notificationResponse = notificationService.create(notificationRequest);
            System.out.println("WS -------- " + notificationResponse);
            simpMessagingTemplate.convertAndSendToUser(notificationResponse.getReceiver().getId(), "/ws/notification", notificationResponse);
        }

    }


}
