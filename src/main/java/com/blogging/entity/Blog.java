package com.blogging.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "blog", indexes = { @Index(name = "idx_blog_author", columnList = "author") })
@NoArgsConstructor
@Getter
@Setter
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false, length = 5000)
    private String content;

    private String image;

    private String summary;

    @Column(nullable = false)
    private String title;

    @ManyToMany(mappedBy = "clappedBlogs")
    @JsonIgnore
    private Set<AppUser> usersWhoClapped = new HashSet<>();

    @ManyToMany(mappedBy = "favoritedBlogs", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<AppUser> favoritedByUsers = new HashSet<>();

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> comments;

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
