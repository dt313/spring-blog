package com.blog.api.controller;

import com.blog.api.dto.request.CommentCreationRequest;
import com.blog.api.dto.response.CommentResponse;
import com.blog.api.entities.ResponseObject;
import com.blog.api.service.CommentService;
import com.blog.api.types.TableType;
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
@RequestMapping("/api/v1/comments")
public class CommentController {
    CommentService commentService;

    @GetMapping("")
    ResponseEntity<ResponseObject> getAllComment() {
        List<CommentResponse> result = commentService.getAllComment();

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK, "Get all comment successfully", result));
    }
    @PostMapping("")
    ResponseEntity<ResponseObject> create(@RequestBody CommentCreationRequest request) {
        CommentResponse result = commentService.create(request);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK, "Create comment successfully", result));
    }

    @GetMapping("/{type}/{commentableId}")
    ResponseEntity<ResponseObject> getCommentsByCommentableId(
            @PathVariable TableType type,
            @PathVariable Long commentableId ,
            @RequestParam(required = false, defaultValue = "1") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize
            ) {
        List<CommentResponse> result = commentService.getByTypeAndCommentableId(type,commentableId, pageNumber,pageSize);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK, "Get all comment by comment table id successfully", result));
    }



    @GetMapping("/{commentsById}")
    ResponseEntity<ResponseObject> getCommentsById(@PathVariable Long commentsById) {
        CommentResponse result = commentService.getById(commentsById);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK, "Get comment successfully", result));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteById(@PathVariable Long id) {
        commentService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK, "Delete comment successfully", null));
    }

}
