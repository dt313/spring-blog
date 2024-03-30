package com.blog.api.models.dto;


import com.blog.api.types.TableType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LikeDTO {

    private Long id;
    private String likeId ;
    private ArticleDTO artId;
    private TableType likeTableType;
    private final Set<UserDTO> likedUsers = new HashSet<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
