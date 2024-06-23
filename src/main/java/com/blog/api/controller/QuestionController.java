package com.blog.api.controller;

import com.blog.api.dto.request.ArticleRequest;
import com.blog.api.dto.request.QuestionRequest;
import com.blog.api.dto.request.SuggestionRequest;
import com.blog.api.dto.response.ArticleResponse;
import com.blog.api.dto.response.QuestionResponse;
import com.blog.api.entities.ResponseObject;
import com.blog.api.service.ArticleService;
import com.blog.api.service.QuestionService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/questions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuestionController {

    QuestionService questionService;
    @GetMapping("")
    private ResponseEntity<ResponseObject> getAll(
            @RequestParam(required = false, defaultValue = "") String searchValue,
            @RequestParam(required = false, defaultValue = "1") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize)
    {
        System.out.println(searchValue);
        List<QuestionResponse> question = questionService.getAll(searchValue,pageNumber, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Query all question successfully", question ));
    }

    @GetMapping("/lengthByValue")
    private ResponseEntity<ResponseObject> getLengthByValue(
            @RequestParam(required = false, defaultValue = "") String searchValue
    )
    {
        Integer length = questionService.getLengthBySearchValue(searchValue);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Query all articles successfully", length ));
    }

    @GetMapping("/lengthByTopic")
    private ResponseEntity<ResponseObject> getLengthByTopic(
            @RequestParam(required = false, defaultValue = "") String name
    )
    {
        Integer length = questionService.getLengthByTopic(name);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Query all articles successfully", length ));
    }

    @GetMapping("/author/{id}")
    private ResponseEntity<ResponseObject> getAllByUserName(@PathVariable String id) {
        List<QuestionResponse> articles = questionService.getAllByAuthor(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Query all articles successfully", articles ));
    }

    @GetMapping("/topic")
    private ResponseEntity<ResponseObject> getAllByTopic(
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "1") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize
    ) {
        List<QuestionResponse> questions = questionService.getAllByTopic(name, pageNumber,pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Query all articles successfully", questions ));
    }


    @GetMapping("/{id}")
    private ResponseEntity<ResponseObject> getById(@PathVariable String id) {
        QuestionResponse question = questionService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Query article succesfully" , question));
    }

    @PostMapping("")
    private ResponseEntity<ResponseObject> create(@RequestBody @Valid QuestionRequest request) {
        QuestionResponse question = questionService.create(request);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "create article succesfully" , question));

    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(@PathVariable String id, @RequestBody QuestionRequest request) {
        QuestionResponse question = questionService.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Update succesfully" , question));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> delete(@PathVariable String id) {
        questionService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Delete article succesfully" , true));

    }

}
