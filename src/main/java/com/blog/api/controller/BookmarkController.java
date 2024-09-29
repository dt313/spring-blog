package com.blog.api.controller;

import com.blog.api.dto.request.BookmarkRequest;
import com.blog.api.dto.response.BookmarkResponse;
import com.blog.api.entities.ResponseObject;
import com.blog.api.service.BookmarkService;
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
@RequestMapping("/api/v1/bookmark")
public class BookmarkController {
    BookmarkService bookmarkService;

    @GetMapping("")
    ResponseEntity<ResponseObject> getAll() {
        List<BookmarkResponse> result = bookmarkService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(
               1000, HttpStatus.OK,"Get all bookmark successfully", result
        ));
    }

    @GetMapping("/byUser/{id}")
    ResponseEntity<ResponseObject> checkIsBookmarked(
            @PathVariable(required = true) Long id
    ) {
        List<BookmarkResponse> result = bookmarkService.getAllArticleBookmarkedByUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(
                1000,HttpStatus.OK,"Get all bookmarks by user successfully", result
        ));
    }


    @PostMapping("/toggle")
    ResponseEntity<ResponseObject> toggle(@RequestBody BookmarkRequest request) {
        boolean isBookmarked =  bookmarkService.toggle(request);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(
                1000,HttpStatus.OK,"toggle bookmark success", isBookmarked
        ));
    }
}
