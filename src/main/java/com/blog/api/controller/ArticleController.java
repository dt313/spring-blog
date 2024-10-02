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

import java.util.List;

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
        List<ArticleResponse> articles = articleService.getAll(searchValue,pageNumber, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK, "Query all articles successfully", articles ));
    }

    @GetMapping("/feature")
    private ResponseEntity<ResponseObject> getAllFeatureArticle(
            @RequestParam(required = false, defaultValue = "") String searchValue,
            @RequestParam(required = false, defaultValue = "1") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize)
    {
        List<ArticleResponse> articles = articleService.getAllFeaturedArticle(searchValue,pageNumber, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK, "Query all feature articles successfully", articles ));
    }

    @GetMapping("/lengthByValue")
    private ResponseEntity<ResponseObject> getLengthByValue(
            @RequestParam(required = false, defaultValue = "") String searchValue
    )
    {
        Integer length = articleService.lengthOfArticleBySearchValue(searchValue);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK, "Query articles length successfully", length ));
    }

    @GetMapping("/lengthByTopic")
    private ResponseEntity<ResponseObject> getLengthByTopic(
            @RequestParam(required = false, defaultValue = "") String name
    )
    {
        Integer length = articleService.lengthOfArticleByTopic(name);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK, "Query articles lengths successfully", length ));
    }

    @GetMapping("/author/{id}")
    private ResponseEntity<ResponseObject> getAllByUserName(@PathVariable Long id) {
        List<ArticleResponse> articles = articleService.getAllArticleByAuthor(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK, "Query all articles by username successfully", articles ));
    }

    @GetMapping("/topic")
    private ResponseEntity<ResponseObject> getAllByTopic(
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "1") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize
    ) {
        List<ArticleResponse> articles = articleService.getAllByTopic(name, pageNumber,pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK, "Query all articles by topic successfully", articles ));
    }

    @PostMapping("/suggestion")
    private ResponseEntity<ResponseObject> getSuggestionsArticle(
            @RequestBody(required = true)SuggestionRequest request
            ) {
        List<ArticleResponse> articles = articleService.getSuggestionsArticle(request);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK, "Query all suggestion articles successfully", articles ));
    }

    @GetMapping("/{id}")
    private ResponseEntity<ResponseObject> getById(@PathVariable Long id) {
        ArticleResponse article = articleService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK, "Query article by id successfully" , article));
    }

    @PostMapping("/publish/{id}")
    public ResponseEntity<ResponseObject> publish(@PathVariable Long id) {
        ArticleResponse response = articleService.publish(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK, "Publish article successfully" , response));

    }
    

    @GetMapping("/slug/{slug}")
    private ResponseEntity<ResponseObject> getBySlug(@PathVariable String slug) {
        ArticleResponse article = articleService.getBySlug(slug);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK, "Query article by slug successfully" , article));
    }

    @GetMapping("/edit/slug/{slug}")
    private ResponseEntity<ResponseObject> getBySlugWithAuth(@PathVariable String slug) {
        ArticleResponse article = articleService.getBySlugWithAuth(slug);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK, "Query article by slug successfully" , article));
    }



    @PostMapping("")
    private ResponseEntity<ResponseObject> create(@RequestBody @Valid ArticleRequest newArticle) {
        ArticleResponse article = articleService.create(newArticle);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK, "create article successfully" , article));

    }

    @PutMapping("/{slug}")
    public ResponseEntity<ResponseObject> update(@PathVariable String slug, @RequestBody ArticleRequest updateArticle) {
        ArticleResponse article = articleService.update(slug, updateArticle);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK, "Update article successfully" , article));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> delete(@PathVariable Long id) {
        articleService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK, "Delete article successfully" , true));

    }


}
