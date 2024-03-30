package com.blog.api.models.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private int age;
    private String fullName;
    private String email;

    private String avatar;
    private String gender;
    private Date birth;
    private String introduction;
    private String linkBio;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}

