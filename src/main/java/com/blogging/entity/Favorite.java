package com.blogging.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

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

    // Constructors
    public Favorite() {}

    public Favorite(Userr userr, Long blogId) {
        this.userr = userr;
        this.blogId = blogId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Userr getUserr() {
        return userr;
    }

    public void setUserr(Userr userr) {
        this.userr = userr;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    @Override
    public String toString() {
        return "Favorite{" +
                "id=" + id +
                ", blogId=" + blogId +
                '}';
    }
}
