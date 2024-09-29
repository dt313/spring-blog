package com.blog.api.controller;

import com.blog.api.dto.request.NotificationRequest;
import com.blog.api.dto.response.NotificationResponse;
import com.blog.api.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

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
        return "PONG";
    }

    @MessageMapping("/notification")
    private void sendNotification(@Payload NotificationRequest notificationRequest) throws Exception
    {

        if(!notificationRequest.getSender().equals(notificationRequest.getReceiver())) {
            NotificationResponse notificationResponse = notificationService.create(notificationRequest);
            if(Objects.nonNull(notificationResponse)) {
                simpMessagingTemplate.convertAndSendToUser(notificationResponse.getReceiver().getId().toString(), "/ws/notification", notificationResponse);

            }
        }

    }


}
