package com.blog.api.service.impl;

import com.blog.api.dto.request.ChatMessage;
import com.blog.api.dto.request.ChatRequest;
import com.blog.api.dto.request.SummarizationRequest;
import com.blog.api.dto.response.ChatResponse;
import com.blog.api.service.AIService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
@Slf4j
public class AIServiceImp implements AIService {

    private final RestTemplate restTemplate = new RestTemplate();

    @NonFinal
    @Value("${spring.ai.ollama.base-url}")
    private String END_POINT;

    @NonFinal
    @Value("${spring.ai.ollama.chat.options.model}")
    private String MODEL_NAME;
    @Override
    public String summarization(SummarizationRequest request) {
        try {
            List<ChatMessage> messages = List.of(
                    new ChatMessage("system", "You are an AI that assists in summarizing article content(about 100 to 200 words). You will return responses in markdown format ."),
                    new ChatMessage("user", request.getContent())
            );

            ChatRequest chatRequest = new ChatRequest(MODEL_NAME,messages, false);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<ChatRequest> requestEntity = new HttpEntity<>(chatRequest, headers);

            ResponseEntity<ChatResponse> response = restTemplate.exchange(
                    END_POINT,
                    HttpMethod.POST,
                    requestEntity,
                    ChatResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                ChatResponse chatResponse = response.getBody();
                log.info(chatResponse.getMessage().getContent());
                return Objects.requireNonNull(chatResponse.getMessage().getContent()); // Trả về nội dung tóm tắt
            } else {
                log.warn("Ollama API returned an unexpected response: {}", response);
            }

        } catch (Exception e) {
            log.error("Error while calling Ollama API: ", e);
        }

        return "";
    }
}
