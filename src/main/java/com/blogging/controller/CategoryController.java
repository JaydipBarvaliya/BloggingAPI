package com.blogging.controller;

import com.blogging.entity.Blog;
import com.blogging.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // Fetch blogs by category
    @GetMapping("/{category}/blogs")
    public ResponseEntity<List<Blog>> getBlogsByCategory(@PathVariable String category) {
        List<Blog> blogs = categoryService.getBlogsByCategory(category);
        return ResponseEntity.ok(blogs);
    }
}
