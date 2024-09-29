package com.blog.api.controller;

import com.blog.api.dto.request.NotificationRequest;
import com.blog.api.dto.response.NotificationResponse;
import com.blog.api.entities.ResponseObject;
import com.blog.api.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {

    NotificationService notificationService;


    @GetMapping("")
    private ResponseEntity<ResponseObject> getAll(
            @RequestParam(required = false, defaultValue = "1") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize
    ) {
        List <NotificationResponse> list = notificationService.getAll(pageNumber,pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK, "Query all notifications successfully", list ));
    }

    @GetMapping("/{id}")
    private ResponseEntity<ResponseObject> getAllByUser(
            @RequestParam(required = false, defaultValue = "1") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @PathVariable Long id
    ) {
        List <NotificationResponse> list = notificationService.getAllByUser(id, pageNumber,pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK, "Query notification successfully", list ));
    }

    @PostMapping("")
    private ResponseEntity<ResponseObject> create(@RequestBody NotificationRequest notificationRequest) {
         NotificationResponse notification = notificationService.create(notificationRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK, "create notification successfully", notification ));
    }


    @GetMapping("/read/{id}")
    private ResponseEntity<ResponseObject> read(@PathVariable Long id) {
        boolean isReaded = notificationService.read(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK, "read notification successfully", isReaded ));
    }

    @GetMapping("/read")
    private ResponseEntity<ResponseObject> readAll() {
        boolean isReadAll = notificationService.readAll();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK, "read all successfully", isReadAll ));
    }
}
