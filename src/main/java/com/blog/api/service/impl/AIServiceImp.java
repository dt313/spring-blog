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
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
@Slf4j
public class AIServiceImp implements AIService {

    private final RestTemplate restTemplate = new RestTemplate();


    @NonFinal
    @Value("${spring.ai.factchat.base-url}")
    private String END_POINT; // https://factchat-cloud.mindlogic.ai/v1/api/openai/chat/completions

    @NonFinal
    @Value("${spring.ai.factchat.key}")
    private String API_KEY;

    @NonFinal
    @Value("${spring.ai.factchat.model}")
    private String MODEL_NAME; // gpt-5-chat-latest
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
            headers.setBearerAuth(API_KEY); // Authorization: Bearer xxx

            HttpEntity<ChatRequest> requestEntity = new HttpEntity<>(chatRequest, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    END_POINT,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();

                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> firstChoice = choices.get(0);
                    Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
                    if (message != null && message.get("content") != null) {
                        String content = message.get("content").toString();
                        log.info("AI Response: {}", content);
                        return content;
                    }
                }
            } else {
                log.warn("Ollama API returned an unexpected response: {}", response);
            }

        } catch (Exception e) {
            log.error("Error while calling Ollama API: ", e);
        }

        return "";
    }
}
