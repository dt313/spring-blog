package com.blog.api.models.entity;
import com.blog.api.service.LikeService;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "articles")
@EntityListeners(AuditingEntityListener.class)
public class Article {

    @SequenceGenerator(
            name="art_sequence",
            sequenceName="art_sequence" ,
            allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="art_sequence")
    @Id
    private Long id;
    @Column
    private String artId = UUID.randomUUID().toString();
    @ManyToOne
    @JoinColumn(name = "author")
    private User author;
    @Column
    private String title;
    @Column
    private String content;
    @Column
    private String type;

    @Autowired
    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private Metadata metadata;
    @Column
    private String artLink;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "art_topic", joinColumns = @JoinColumn(name = "art_id") , inverseJoinColumns = @JoinColumn(name = "topic_id"))
    private Set<Topic> topics = new HashSet<>();

    @Column
    private boolean isApproved;
    @Column
    private boolean isDeleted;

    @Transient
    private Integer likeCount = 0;
    @Transient
    private Integer commentCount = 0;
    @Transient
    private Integer bookmarkCount = 0;


    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;


    public Article(User author, String title, String content, Integer like_count, Integer comment_count, Metadata metadata, Set<Topic> topics) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.likeCount = like_count;
        this.commentCount = comment_count;
        this.artId = UUID.randomUUID().toString();
        this.artLink = "no_link";
        this.type = "article";
    }

}
