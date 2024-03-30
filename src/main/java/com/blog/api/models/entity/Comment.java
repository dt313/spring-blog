package com.blog.api.models.entity;

import com.blog.api.types.TableType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EntityListeners(AuditingEntityListener.class)
@Table(name = "comments")
public class Comment {
    @Id
    @SequenceGenerator(name = "comment_sequence", sequenceName = "comment_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_sequence" )
    private Long id;
    @Column
    private String cmtId = UUID.randomUUID().toString();
    @ManyToOne
    @JoinColumn(name = "art_id" , referencedColumnName = "id")
    private Article artId;
    @ManyToOne
    @JoinColumn(name = "user_id" , referencedColumnName = "id")
    private User cmtUser;
    @Column
    private Long parentId;
    @Column
    @Enumerated(EnumType.STRING)
    private TableType tableType;
    @Column(name = "is_approved")
    private boolean isApproved = true;
    @Column
    private String content;
    @OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.REMOVE)
    private List<Comment> replies = new ArrayList<Comment>();



    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;


    public Comment(Article artId, User cmtUser, Long parentId, TableType tableType, String content) {
        this.artId = artId;
        this.cmtUser = cmtUser;
        this.parentId = parentId;
        this.tableType = tableType;
        this.content = content;
    }
}
