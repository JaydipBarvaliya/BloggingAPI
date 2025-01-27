package com.blogging.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // The name of the person who made the comment

    private String comment; // The actual content of the comment

    private String userId; // The ID of the user who posted the comment

    private LocalDateTime timestamp; // The time when the comment was posted

    @ManyToOne
    @JoinColumn(name = "blog_id", nullable = false)
    @JsonBackReference
    private Blog blog; // The blog associated with this comment

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    // Override equals and hashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment1 = (Comment) o;
        return Objects.equals(id, comment1.id) &&
                Objects.equals(name, comment1.name) &&
                Objects.equals(comment, comment1.comment) &&
                Objects.equals(userId, comment1.userId) &&
                Objects.equals(timestamp, comment1.timestamp) &&
                Objects.equals(blog, comment1.blog);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, comment, userId, timestamp, blog);
    }

    // Override toString

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", userId='" + userId + '\'' +
                ", timestamp=" + timestamp +
                ", blog=" + blog +
                '}';
    }
}
