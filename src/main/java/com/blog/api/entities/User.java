package com.blog.api.entities;

import com.blog.api.validation.DobConstraint;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
     Long id;
     @Column(nullable = false, unique = true)
     String email;
     @Column(nullable = false, unique = true)
     String username;
     String name;
     String password;
     @DobConstraint(min=12)
     LocalDate dob;
     String lastname = "";
     String firstname = "";
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "roleOfUser",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_name"))
    Set<Role> roles;
    @OneToMany(mappedBy = "id", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    Set<Article> articles;
    @OneToMany(mappedBy = "bookmarkTableId",cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    List<Bookmark> bookmarks;

//    private String gender = "";
    private String avatar = "";

    private String bio = "";
    private String web_link = "";
    private String fb_link = "";
    private String gh_link = "";
    private String ig_link = "";
    private String lk_link = "";

//    @Column
//    private boolean isAdmin;
//    @Column
//    private boolean isPro;
//    @Column
//    private boolean isDeleted;
    //  date
    @Column(updatable = false)
    @CreationTimestamp
    Instant createdAt;
    @UpdateTimestamp
    Instant updatedAt;


}
