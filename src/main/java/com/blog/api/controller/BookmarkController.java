package com.blog.api.controller;

import com.blog.api.models.dto.ArticleDTO;
import com.blog.api.models.dto.BookmarkDTO;
import com.blog.api.models.entity.Bookmark;
import com.blog.api.models.entity.ResponseObject;
import com.blog.api.models.request.BookmarkRequest;
import com.blog.api.repository.BookmarkRepository;
import com.blog.api.service.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bookmark")
public class BookmarkController {
    @Autowired
    private BookmarkService bookmarkService;
    @PostMapping("/toggle")
    private ResponseEntity<ResponseObject> toggleBookmark(@RequestBody BookmarkRequest newBookmark) {
        System.out.println(newBookmark);
        BookmarkDTO result = bookmarkService.toggleBookmark(newBookmark);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "deleted bookmark successfully", false));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "bookmark successfully", true));

    }

    @GetMapping("/user/{id}")
    private ResponseEntity<ResponseObject> getAllBookmarkByUser(@PathVariable Long id) {
        System.out.println("Here");
        List<BookmarkDTO> result = bookmarkService.getAllBookmarkByUserId(id);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "query all bookamrk succesfully", result));
    }

}
