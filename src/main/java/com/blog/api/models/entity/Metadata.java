package com.blog.api.models.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.repository.Query;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Metadata {
    @Id
    @SequenceGenerator(
            name = "metadata_sequence",
            sequenceName = "metadata_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "metadata_sequence")
    private Long id;
    @Column
    private String title;
    @Column
    private String description;
    @Column
    private String thumbnail;


    public Metadata( String title, String description, String thumbnail) {
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
    }
}
