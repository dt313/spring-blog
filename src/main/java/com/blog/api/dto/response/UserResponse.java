package com.blog.api.dto.response;

import com.blog.api.entities.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
     Long id;
     String email;
     LocalDate dob;
     String username;
     String name;
     String password;
     String avatar;
     Set<Role> roles;
     @JsonProperty("created_at")
     Instant createdAt;
     @JsonProperty("updatedAt_at")
     Instant updatedAt;

}

