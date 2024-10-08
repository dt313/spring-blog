package com.blog.api.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SuggestionRequest {
    Long userId;
    Set<String> topics;
}
