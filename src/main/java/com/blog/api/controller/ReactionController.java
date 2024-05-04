package com.blog.api.controller;

import com.blog.api.dto.request.ReactionRequest;
import com.blog.api.dto.response.ReactionResponse;
import com.blog.api.entities.ResponseObject;
import com.blog.api.service.ReactionService;
import com.blog.api.types.ReactionTableType;
import com.blog.api.types.ReactionType;
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
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK,"success",result));
    }

    @PostMapping("/toggle")
    ResponseEntity<ResponseObject> toggle(@RequestBody ReactionRequest request) {
        reactionService.toggleReaction(request);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK,"success",null));
    }

    @GetMapping("/{type}/{id}")
    ResponseEntity<ResponseObject> getAllByReactionTableId(@PathVariable ReactionTableType type, @PathVariable String id) {
        List<ReactionResponse> result = reactionService.getAllByReactionTableId(type,id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK,"success",result));
    }

    @GetMapping("/length")
    ResponseEntity<ResponseObject> countOfReaction() {
        Integer result = reactionService.countOfReaction();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK,"success",result));
    }

    @GetMapping("/length/{id}")
    ResponseEntity<ResponseObject> countOfReactionByReactionTableId(@PathVariable String id) {
        Integer result = reactionService.countOfReactionByReactionTableId(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK,"success",result));
    }

    @GetMapping("/check")
    ResponseEntity<ResponseObject> checkReaction(
            @RequestParam(required = true ) ReactionTableType reactionTableType,
            @RequestParam(required = true) String reactionTableId,
            @RequestParam(required = true) String userId
    ) {
        ReactionType result = reactionService.checkReaction(reactionTableType,reactionTableId,userId);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK,"success",result));
    }

}
