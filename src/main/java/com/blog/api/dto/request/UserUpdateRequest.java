package com.blog.api.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserUpdateRequest {
    String email;
    String avatar;
    String name;
    String bio;
    String web_link;
    String fb_link;
    String gh_link;
    String lk_link;
    String ig_link;
    String password;
    String username;
    LocalDate dob;
}
