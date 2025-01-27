package com.blogging.controller;

import com.blogging.entity.Blog;
import com.blogging.repository.BlogRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final BlogRepository blogRepository;

    public CategoryController(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @GetMapping
    public ResponseEntity<?> getCategories() {
        List<String> categories = blogRepository.findDistinctCategories();

        if (categories.isEmpty()) {
            return ResponseEntity.status(404).body("No categories found");
        }

        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{category}/blogs")
    public ResponseEntity<?> getBlogsByCategory(@PathVariable String category) {
        List<Blog> blogs = blogRepository.findByCategory(category);

        if (blogs.isEmpty()) {
            return ResponseEntity.status(404).body("No blogs found for category: " + category);
        }

        return ResponseEntity.ok(blogs);
    }
}
