package com.blog.api.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id

    @GeneratedValue(strategy=GenerationType.UUID)
     String id;
     String email;
     String username;
     String password;
     LocalDate dob;
     String lastname = "";
     String firstname = "";
    @ManyToMany()
    @JoinTable(name = "roleOfUser",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_name"))
    Set<Role> roles;
    @OneToMany(mappedBy = "id", cascade = CascadeType.REMOVE)
    Set<Article> articles;
    @OneToMany(mappedBy = "bookmarkTableId",cascade = CascadeType.REMOVE)
    List<Bookmark> bookmarks;

//    private String gender = "";
//    private String avatar = "";
//    @Column
//    private String introduction = "";
//    @Column
//    private String linkBio = "";
//    @Column
//    private boolean isAdmin;
//    @Column
//    private boolean isPro;
//    @Column
//    private boolean isDeleted;

    //  date
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;


}
