package com.blog.api.controller;


import com.blog.api.models.dto.NotificationDTO;
import com.blog.api.models.entity.ResponseObject;
import com.blog.api.models.request.NotificationRequest;
import com.blog.api.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @GetMapping("")
    private ResponseEntity<ResponseObject> getAllNotifications() {
            List<NotificationDTO> result = notificationService.getALlNotifications();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "success", result));
    }

    @GetMapping("/{id}")
    private ResponseEntity<ResponseObject> getNotificationById(@PathVariable Long id) {
        NotificationDTO result = notificationService.getNotificationById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "success", result));
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<ResponseObject> deleteNotificationById(@PathVariable Long id) {
        boolean result = notificationService.deleteNotificationById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "success", result));
    }

    @GetMapping("/target/{id}")
    private ResponseEntity<ResponseObject> getNotificationByTarget(@PathVariable Long id) {
        List<NotificationDTO> result = notificationService.getNotificationByTarget(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "success", result));
    }

    @GetMapping("/sender/{id}")
    private ResponseEntity<ResponseObject> getNotificationBySender(@PathVariable Long id) {
        List<NotificationDTO> result = notificationService.getNotificationBySender(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "success", result));
    }

    @PostMapping("")
    private ResponseEntity<ResponseObject> createNotification(@RequestBody NotificationRequest notificationRequest) {
        NotificationDTO result = notificationService.createNotification(notificationRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "success", result));
    }

    @PostMapping("/read/all/{id}")
    private ResponseEntity<ResponseObject> readAllNotificationByTargetId(@PathVariable Long id) {
        List<NotificationDTO> result = notificationService.readAllNotificationByTarget(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "success", result));
    }


    @PutMapping("/read/{id}")
    private ResponseEntity<ResponseObject> readNotificationById(@PathVariable Long id) {
        NotificationDTO result = notificationService.readNotificationById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "success", result));
    }



}
