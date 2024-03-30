package com.blog.api.controller;

import com.blog.api.models.dto.LikeDTO;
import com.blog.api.models.dto.UserDTO;
import com.blog.api.models.entity.ResponseObject;
import com.blog.api.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/like")
public class LikeController {

    @Autowired
    private LikeService likeService;
    @GetMapping("/from/{userId}/to/{articleId}")
    private ResponseEntity<ResponseObject> toggleLike(@PathVariable Long userId, @PathVariable Long articleId) {
        LikeDTO result = likeService.toggleLike(userId,articleId);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Liked succesfully", result));
    }

    @GetMapping("/{byArtId}")
    private ResponseEntity<ResponseObject> getAllLikedUser(@PathVariable Long byArtId) {
        Set<UserDTO> result = likeService.getAllLikedUser(byArtId);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Query all liked user of article succesfully", result));
    }

    @GetMapping("/{userId}/liked/{articleId}")
    private ResponseEntity<ResponseObject> getIsLiked(@PathVariable Long userId, @PathVariable Long articleId) {
        boolean result = likeService.isLiked(userId,articleId);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Liked succesfully", result));
    }
}
