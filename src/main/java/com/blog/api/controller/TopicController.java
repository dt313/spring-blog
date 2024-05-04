package com.blog.api.controller;

import com.blog.api.entities.ResponseObject;
import com.blog.api.entities.Topic;
import com.blog.api.service.TopicService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/topics")
public class TopicController {
    TopicService topicService;
    @GetMapping("")
    ResponseEntity<ResponseObject> getAll() {
        List<Topic> topics = topicService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(HttpStatus.OK, "success", topics));
    }

    @GetMapping("/{name}")
    ResponseEntity<ResponseObject> getAll(@PathVariable String name) {
        Topic topic = topicService.getByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(HttpStatus.OK, "success", topic));
    }

    @DeleteMapping("/{name}")
    ResponseEntity<ResponseObject> delete(@PathVariable String name) {
        topicService.delete(name);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(HttpStatus.OK, "success",null));
    }
}
