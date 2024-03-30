package com.blog.api.controller;

import com.blog.api.models.dto.ArticleDTO;
import com.blog.api.models.entity.Article;
import com.blog.api.models.entity.ResponseObject;
import com.blog.api.models.request.ArticleRequest;
import com.blog.api.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping("")
    private ResponseEntity<ResponseObject> getAllArticles() {
        List<ArticleDTO> articles = articleService.getAllArticle();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Query all articles successfully", articles ));
    }

    @GetMapping("/{id}")
    private ResponseEntity<ResponseObject> getArticleById(@PathVariable Long id) {
        ArticleDTO article = articleService.getArticleById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Query article succesfully" , article));

    }

    @PostMapping("")
    private ResponseEntity<ResponseObject> insertArticle(@RequestBody ArticleRequest newArticle) {
        ArticleDTO article = articleService.insertArticle(newArticle);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Insert article succesfully" , article));

    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateArticle(@PathVariable Long id, @RequestBody ArticleRequest updateArticle) {
        ArticleDTO article = articleService.updateArticle(id, updateArticle);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Update succesfully" , article));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteArticle(@PathVariable Long id) {
        boolean isDeleted = articleService.deleteArticle(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Delete article succesfully" , ""));

    }

}
