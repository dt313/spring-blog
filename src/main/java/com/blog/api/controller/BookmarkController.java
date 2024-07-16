package com.blog.api.controller;

import com.blog.api.dto.request.BookmarkRequest;
import com.blog.api.dto.response.ArticleResponse;
import com.blog.api.dto.response.BookmarkResponse;
import com.blog.api.entities.ResponseObject;
import com.blog.api.service.BookmarkService;
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
@RequestMapping("/api/v1/bookmark")
public class BookmarkController {
    BookmarkService bookmarkService;

    @GetMapping("")
    ResponseEntity<ResponseObject> getAll() {
        List<BookmarkResponse> result = bookmarkService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(
                HttpStatus.OK,"success", result
        ));
    }

    @GetMapping("/byUser/{id}")
    ResponseEntity<ResponseObject> checkIsBookmarked(
            @PathVariable(required = true) String id
    ) {
        List<BookmarkResponse> result = bookmarkService.getAllArticleBookmarkedByUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(
                HttpStatus.OK,"success", result
        ));
    }

    @GetMapping("/length/{id}")
    ResponseEntity<ResponseObject> countOfBookmarkByBookmarkTableId(@PathVariable String id ) {
        Integer result = bookmarkService.countOfBookmarkByBookmarkTableId(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(
                HttpStatus.OK,"success", result
        ));
    }

    @GetMapping("/check")
    ResponseEntity<ResponseObject> checkIsBookmarked(
            @RequestParam(required = true ) TableType tableType,
            @RequestParam(required = true) String bookmarkTableId,
            @RequestParam(required = true) String userId
    ) {
        boolean result = bookmarkService.checkIsBookmarked(tableType,bookmarkTableId,userId);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(
                HttpStatus.OK,"success", result
        ));
    }

    @PostMapping("/toggle")
    ResponseEntity<ResponseObject> toggle(@RequestBody BookmarkRequest request) {
        boolean isBookmarked =  bookmarkService.toggle(request);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(
                HttpStatus.OK,"success", isBookmarked
        ));
    }
}
