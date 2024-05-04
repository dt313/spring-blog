package com.blog.api.controller;

import com.blog.api.dto.request.ArticleRequest;
import com.blog.api.dto.response.ArticleResponse;
import com.blog.api.entities.ResponseObject;
import com.blog.api.service.ArticleService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ArticleController {

    ArticleService articleService;
    @GetMapping("")
    private ResponseEntity<ResponseObject> getAll() {
        List<ArticleResponse> articles = articleService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Query all articles successfully", articles ));
    }

    @GetMapping("/{id}")
    private ResponseEntity<ResponseObject> getById(@PathVariable String id) {
        ArticleResponse article = articleService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Query article succesfully" , article));
    }

    @PostMapping("")
    private ResponseEntity<ResponseObject> create(@RequestBody @Valid ArticleRequest newArticle) {
        ArticleResponse article = articleService.create(newArticle);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "create article succesfully" , article));

    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(@PathVariable String id, @RequestBody ArticleRequest updateArticle) {
        ArticleResponse article = articleService.update(id, updateArticle);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Update succesfully" , article));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> delete(@PathVariable String id) {
        articleService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Delete article succesfully" , true));

    }

}
