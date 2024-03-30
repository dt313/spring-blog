package com.blog.api.models.entity;

import com.blog.api.types.TableType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "likes")
@EntityListeners(AuditingEntityListener.class)
public class Like {
    @Id
    @SequenceGenerator(name = "like_sequence" , sequenceName = "like_sequence" , allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "like_sequence")
    private Long id;
    @Column
    private String likeId = UUID.randomUUID().toString();
    @OneToOne()
    @JoinColumn(name = "art_id" )
    private Article artId;  
    @Column
    @Enumerated(EnumType.STRING)
    private TableType likeTableType;
    @ManyToMany(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinTable(name = "liked_user", joinColumns = @JoinColumn(name = "liked_id") , inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> likedUsers = new HashSet<>();
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @Column
    @LastModifiedDate
    private LocalDateTime updatedAt;


    public Like(Article artId, TableType likeTableType, User likedUsers) {
        this.artId = artId;
        this.likeTableType = likeTableType;
        this.likedUsers.add(likedUsers);
    }

    public  Integer getIndexOfUserInLikedUsers( User element) {
        Iterator<User> iterator = this.likedUsers.iterator();

        int index = 0;
        while (iterator.hasNext()) {
            if (element.equals(iterator.next())) {
                return index;
            }
            index++;
        }
        return -1;
    }


}
