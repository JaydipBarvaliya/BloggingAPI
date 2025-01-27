package com.blogging.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long blogId;

    @Column(nullable = false)
    private String userId;

    public Like(Long blogId, String userId) {
        this.blogId = blogId;
        this.userId = userId;
    }
}
