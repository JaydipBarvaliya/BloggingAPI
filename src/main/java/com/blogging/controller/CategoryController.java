package com.blogging.controller;

import com.blogging.entity.Blog;
import com.blogging.repository.BlogRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final BlogRepository blogRepository;

    public CategoryController(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @GetMapping
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = blogRepository.findDistinctCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{category}/blogs")
    public ResponseEntity<List<Blog>> getBlogsByCategory(@PathVariable String category) {
        List<Blog> blogs = blogRepository.findByCategory(category);
        return ResponseEntity.ok(blogs);
    }
}