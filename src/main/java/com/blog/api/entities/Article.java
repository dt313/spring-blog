package com.blog.api.entities;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "articles")
@EntityListeners(AuditingEntityListener.class)
public class Article {


    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    String id;
    @ManyToOne()
    User author;
    String title;
    String content;
    String metaTitle;
    String description;
    String thumbnail;
    @ManyToMany()
    @JoinTable(
            name = "topicOfArticle",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "topic_id")
    )
    Set<Topic> topics;
    @OneToMany(mappedBy = "artId",cascade = CascadeType.REMOVE)
    List<Comment> comments;

    @OneToMany(mappedBy = "reactionTableId",cascade = CascadeType.REMOVE)
    List<Reaction> reactions;

    @OneToMany(mappedBy = "bookmarkTableId",cascade = CascadeType.REMOVE)
    List<Bookmark> bookmarks;

//    @Column
//    private boolean isApproved;
//    @Column
//    private boolean isDeleted;
//    private Integer likeCount = 0;
//    private Integer commentCount = 0;
//    private Integer bookmarkCount = 0;

//
//    @CreatedDate
//    @Column(updatable = false)
//    private LocalDateTime createdAt;
//    @LastModifiedDate
//    private LocalDateTime updatedAt;


}
