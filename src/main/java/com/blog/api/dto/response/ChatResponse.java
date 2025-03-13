package com.blog.api.dto.response;

import com.blog.api.dto.request.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    private String model;
    private ChatMessage message;
    private boolean done;
}
