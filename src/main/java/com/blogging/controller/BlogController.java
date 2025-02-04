package com.blogging.controller;

import com.blogging.DTO.BlogDTO;
import com.blogging.entity.Blog;
import com.blogging.service.BlogService;
import com.blogging.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {

    private final BlogService blogService;
    private final CategoryService categoryService;


    public BlogController(BlogService blogService, CategoryService categoryService) {
        this.blogService = blogService;
        this.categoryService = categoryService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Blog> createBlog(
            @RequestParam("author") String author,
            @RequestParam("category") String category,
            @RequestParam("content") String content,
            @RequestParam("image") MultipartFile image,
            @RequestParam("summary") String summary,
            @RequestParam("title") String title) throws IOException {

        Blog createdBlog =  blogService.createBlog(author, category, content, image, summary, title);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBlog);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Blog> updateBlog(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam String author,
            @RequestParam String category,
            @RequestParam String summary,
            @RequestParam String content,
            @RequestParam(required = false) MultipartFile image
    ) {
        try {
            Blog updatedBlog = blogService.updateBlog(id, title, author, category, summary, content, image);
            return ResponseEntity.ok(updatedBlog);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null); // Bad Request if any error
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{blogId}")
    public ResponseEntity<String> deleteBlog(@PathVariable Long blogId) {
        try {
            blogService.deleteBlogAndRelatedData(blogId);
            return ResponseEntity.ok("Blog and related data deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to delete blog: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping
    public ResponseEntity<List<BlogDTO>> getAllBlogs() {
        List<BlogDTO> blogDTOs = blogService.getAllBlogs();
        return ResponseEntity.ok(blogDTOs);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{slug}")
    public ResponseEntity<Object> getBlogBySlug(@PathVariable String slug) {
        Blog blog = blogService.findBySlug(slug);  // Find blog by slug
        if (blog != null) {
            return ResponseEntity.ok(blog);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Slug not found for requested URL");  // Return 404 if not found
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{category}/blogs")
    public ResponseEntity<List<Blog>> getBlogsByCategory(@PathVariable String category) {
        List<Blog> blogs = categoryService.getBlogsByCategory(category);
        return ResponseEntity.ok(blogs);
    }

}