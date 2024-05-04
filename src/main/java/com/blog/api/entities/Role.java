package com.blog.api.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name="roles")
public class Role {
    @Id
    String name;
    String description;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "permissionOfRole")
    Set<Permission> permissions;
}
