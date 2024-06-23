package com.blog.api.dto.response;

import com.blog.api.entities.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
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
     String id;
     String email;
     LocalDate dob;
     String username;
     String password;
     Set<Role> roles;
     Instant createdAt;
     Instant updatedAt;

}

