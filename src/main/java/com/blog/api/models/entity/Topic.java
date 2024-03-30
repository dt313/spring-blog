package com.blog.api.models.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Topic {
    @Id
    @SequenceGenerator(
            name = "topic_sequence",
            sequenceName = "topic_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "topic_sequence")
    private Long id;
    @Column
    private String name;
    @Column
    private Integer count;

    @ManyToMany(mappedBy = "topics")
    @Column
    private Set<Article> articles = new HashSet<>();
    public Topic(String topic) {
        this.name = topic;
        this.count = 0;
    }
}
