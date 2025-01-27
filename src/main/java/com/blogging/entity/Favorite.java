package com.blogging.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference // Prevents infinite recursion in serialization
    @JoinColumn(name = "user_id", nullable = false)
    private Userr userr;

    @Column(name = "blog_id", nullable = false)
    private Long blogId;

    public Favorite(Userr userr, Long blogId) {
        this.userr = userr;
        this.blogId = blogId;
    }
}
