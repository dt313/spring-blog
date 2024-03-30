package com.blog.api.models.request;

import com.blog.api.models.entity.Article;
import com.blog.api.models.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookmarkRequest {

    private Long artId;
    private Long userId;

}
