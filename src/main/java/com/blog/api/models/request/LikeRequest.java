package com.blog.api.models.request;

import com.blog.api.models.entity.Article;
import com.blog.api.models.entity.User;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LikeRequest {
    private Article artId;
    private String likeTableType;
    private User likedUser;
}
