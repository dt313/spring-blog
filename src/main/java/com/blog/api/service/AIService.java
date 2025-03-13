package com.blog.api.service;

import com.blog.api.dto.request.ChatMessage;
import com.blog.api.dto.request.SummarizationRequest;
import org.springframework.stereotype.Service;

@Service
public interface AIService {
    String summarization(SummarizationRequest request);
}
