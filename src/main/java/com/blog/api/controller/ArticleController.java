package com.blog.api.controller;

import com.blog.api.dto.request.ArticleRequest;
import com.blog.api.dto.request.SuggestionRequest;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ArticleController {

    ArticleService articleService;
    @GetMapping("")
    private ResponseEntity<ResponseObject> getAll(
            @RequestParam(required = false, defaultValue = "") String searchValue,
            @RequestParam(required = false, defaultValue = "1") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize)
    {
        System.out.println(searchValue);
        List<ArticleResponse> articles = articleService.getAll(searchValue,pageNumber, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Query all articles successfully", articles ));
    }

    @GetMapping("/lengthByValue")
    private ResponseEntity<ResponseObject> getLengthByValue(
            @RequestParam(required = false, defaultValue = "") String searchValue
    )
    {
        Integer length = articleService.lengthOfArticleBySearchValue(searchValue);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Query all articles successfully", length ));
    }

    @GetMapping("/lengthByTopic")
    private ResponseEntity<ResponseObject> getLengthByTopic(
            @RequestParam(required = false, defaultValue = "") String name
    )
    {
        Integer length = articleService.lengthOfArticleByTopic(name);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Query all articles successfully", length ));
    }

    @GetMapping("/author/{id}")
    private ResponseEntity<ResponseObject> getAllByUserName(@PathVariable String id) {
        List<ArticleResponse> articles = articleService.getAllArticleByAuthor(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Query all articles successfully", articles ));
    }

    @GetMapping("/topic")
    private ResponseEntity<ResponseObject> getAllByTopic(
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "1") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize
    ) {
        List<ArticleResponse> articles = articleService.getAllByTopic(name, pageNumber,pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Query all articles successfully", articles ));
    }

    @PostMapping("/suggestion")
    private ResponseEntity<ResponseObject> getSuggestionsArticle(
            @RequestBody(required = true)SuggestionRequest request
            ) {
        List<ArticleResponse> articles = articleService.getSuggestionsArticle(request);
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
