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

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "success", result));
    }
    @PostMapping("")
    ResponseEntity<ResponseObject> create(@RequestBody CommentCreationRequest request) {
        CommentResponse result = commentService.create(request);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "success", result));
    }

    @GetMapping("/{type}/{artId}")
    ResponseEntity<ResponseObject> getCommentsByArtId(
            @PathVariable TableType type,
            @PathVariable String artId ,
            @RequestParam(required = false, defaultValue = "0", value ="pNumber") int pageNumber,
            @RequestParam(required = false, defaultValue = "10", value ="pSize") int pageSize
            ) {
        List<CommentResponse> result = commentService.getCommentByTypeAndArtId(type,artId, pageNumber,pageSize);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "success", result));
    }

    @GetMapping("/{type}/{artId}/comment/{parentId}")
    ResponseEntity<ResponseObject> getCommentsByArtAndParentId(
            @PathVariable(required = false) TableType type,
            @PathVariable(required = true) String artId,
            @PathVariable(required = false) String parentId,
            @RequestParam(required = false, defaultValue = "0", value ="pNumber") int pageNumber,
            @RequestParam(required = false, defaultValue = "10", value ="pSize") int pageSize
    )
    {
        List<CommentResponse> result = commentService.getCommentsByArtAndParentId(type,artId,parentId,pageNumber,pageSize);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "success", result));
    }


    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> getCommentsById(@PathVariable String id) {
        CommentResponse result = commentService.getCommentById(id);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "success", result));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteById(@PathVariable String id) {
        commentService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "success", null));
    }
}
