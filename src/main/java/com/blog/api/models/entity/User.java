package com.blog.api.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @SequenceGenerator(
                        name="user_sequence",
                        sequenceName="user_sequence" ,
                        allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="user_sequence")
    private Long id;
    // random no duplicate
    @Column(nullable = false)
    private String userId = UUID.randomUUID().toString();;
    @Column(nullable = false , unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false, unique = true)
    private String email;

    // nullable field
    @Column
    private String gender = "";
    @Column
    private Date birth = new Date();
    @Column
    private String avatar = "";
    @Column
    private String introduction = "";
    @Column
    private String linkBio = "";

    @Column
    private boolean isAdmin;
    @Column
    private boolean isPro;
    @Column
    private boolean isDeleted;

    //  date
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    // transient
    @Transient
    private int age;

    @Transient
    private String fullName;


//    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Notificaiton> notifications = new ArrayList<>();


//    @Transient
//    private int notification_count;
//    @Transient
//    private int notification_count_unread_count;

    public int getAge() {
        return Calendar.getInstance().get(Calendar.YEAR) ;
    }
    public String getFullname() {
        return this.firstName + " "+ this.lastName;
    }

    public User( String username, String password, String lastName, String firstName, String email) {
        this.username = username;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
    }
}
