package com.blog.api.controller;

import com.blog.api.models.dto.CommentDTO;
import com.blog.api.models.entity.Comment;
import com.blog.api.models.entity.ResponseObject;
import com.blog.api.models.request.CommentRequest;
import com.blog.api.service.CommentService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;
    @PostMapping("/create")
    private ResponseEntity<ResponseObject> createComment(@RequestBody CommentRequest comment) {
        CommentDTO result = commentService.createComment(comment);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "create comment successfully", result));
    }

    @GetMapping("/{id}")
    private ResponseEntity<ResponseObject> getCommentByCommentId(@PathVariable Long id) {
        List<CommentDTO> result = commentService.getCommentByCommentId(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "get comment successfully", result));
    }

    @GetMapping("")
    private ResponseEntity<ResponseObject> getAll() {
        List<CommentDTO> result = commentService.getAllComment();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "get all comment successfully", result));
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<ResponseObject> deleteCommentById(@PathVariable Long id) {
        boolean result = commentService.deleteCommentById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "delete comment successfully", result));
    }
}
