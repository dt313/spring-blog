package com.blog.api.controller;

import com.blog.api.dto.request.ChatMessage;
import com.blog.api.dto.request.ChatRequest;
import com.blog.api.dto.request.Message;
import com.blog.api.dto.request.SummarizationRequest;
import com.blog.api.dto.response.ArticleResponse;
import com.blog.api.dto.response.ChatResponse;
import com.blog.api.entities.ResponseObject;
import com.blog.api.service.AIService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

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
