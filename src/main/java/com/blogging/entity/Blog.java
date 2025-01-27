package com.blogging.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String summary;
    private String content;
    private String image;
    private String author;
    private String category;

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL)
    private List<Comment> comments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Blog blog = (Blog) o;
        return Objects.equals(id, blog.id) && Objects.equals(title, blog.title) && Objects.equals(summary, blog.summary) && Objects.equals(content, blog.content) && Objects.equals(image, blog.image) && Objects.equals(author, blog.author) && Objects.equals(category, blog.category) && Objects.equals(comments, blog.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, summary, content, image, author, category, comments);
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", content='" + content + '\'' +
                ", image='" + image + '\'' +
                ", author='" + author + '\'' +
                ", category='" + category + '\'' +
                ", comments=" + comments +
                '}';
    }
}
