package com.blog.api.controller;

import com.blog.api.dto.request.SummarizationRequest;
import com.blog.api.entities.ResponseObject;
import com.blog.api.service.AIService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AIController {

    private AIService aiService;
    @PostMapping("/summarization")
    private ResponseEntity<ResponseObject> summarize(@RequestBody SummarizationRequest request)
    {
        String response = aiService.summarization(request);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(1000,HttpStatus.OK, "Query all articles successfully", response ));
    }

}
