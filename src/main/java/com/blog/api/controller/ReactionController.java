package com.blog.api.controller;

import com.blog.api.dto.request.ReactionRequest;
import com.blog.api.dto.response.ReactionResponse;
import com.blog.api.entities.ResponseObject;
import com.blog.api.service.ReactionService;
import com.blog.api.types.ReactionType;
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
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping("/api/v1/reaction")
public class ReactionController {
    ReactionService reactionService;
    @GetMapping("")
    ResponseEntity<ResponseObject> getAll() {
        List<ReactionResponse> result = reactionService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK,"Get all reactions successfully",result));
    }

    @PostMapping("/toggle")
    ResponseEntity<ResponseObject> toggle(@RequestBody ReactionRequest request) {
        ReactionResponse response = reactionService.toggleReaction(request);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK,"Toggle reaction success",response));
    }

    @GetMapping("/{type}/{id}")
    ResponseEntity<ResponseObject> getAllByReactionTableId(@PathVariable TableType type, @PathVariable Long id) {
        List<ReactionResponse> result = reactionService.getAllByReactionTableId(type,id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK,"Get all by reaction table id successfully",result));
    }


}
