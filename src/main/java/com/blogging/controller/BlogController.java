package com.blogging.controller;

import com.blogging.DTO.BlogDTO;
import com.blogging.DTO.CommentDTO;
import com.blogging.entity.Blog;
import com.blogging.service.BlogService;
import com.blogging.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {

    private final BlogService blogService;
    private final CommentService commentService;

    public BlogController(BlogService blogService, CommentService commentService) {
        this.blogService = blogService;
        this.commentService = commentService;
    }


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


    @DeleteMapping("/{blogId}")
    public ResponseEntity<String> deleteBlog(@PathVariable Long blogId) {
        try {
            blogService.deleteBlogAndRelatedData(blogId);
            return ResponseEntity.ok("Blog and related data deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to delete blog: " + e.getMessage());
        }
    }




    @GetMapping
    public ResponseEntity<List<BlogDTO>> getAllBlogs() {
        List<BlogDTO> blogDTOs = blogService.getAllBlogs();
        return ResponseEntity.ok(blogDTOs);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Blog> getBlogBySlug(@PathVariable String slug) {
        Blog blog = blogService.findBySlug(slug);  // Find blog by slug
        if (blog != null) {
            return ResponseEntity.ok(blog);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Return 404 if not found
        }
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDTO>> getCommentsByBlogId(@PathVariable Long id) {
        List<CommentDTO> commentDTOs = commentService.getCommentsByBlogId(id);
        return ResponseEntity.ok(commentDTOs);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDTO> addCommentToBlog(@PathVariable Long id, @RequestBody CommentDTO commentDTO) {
        CommentDTO createdComment = commentService.addCommentToBlog(id, commentDTO);
        return ResponseEntity.ok(createdComment);
    }











    // ✅ Toggle Favorite
    @PostMapping("/{blogId}/favorite/{userId}")
    public ResponseEntity<String> toggleFavorite(@PathVariable Long blogId, @PathVariable Long userId) {
        blogService.toggleFavorite(blogId, userId);
        return ResponseEntity.ok("Favorite toggled successfully");
    }

    // ✅ Check if Favorited
    @GetMapping("/{blogId}/favorited/{userId}")
    public ResponseEntity<Boolean> isBlogFavoritedByUser(@PathVariable Long blogId, @PathVariable Long userId) {
        boolean favorited = blogService.isBlogFavoritedByUser(blogId, userId);
        return ResponseEntity.ok(favorited);
    }

    @GetMapping("/favorited/{userId}")
    public ResponseEntity<List<Blog>> getUserFavorites(@PathVariable Long userId) {
        List<Blog> favoriteBlogs = blogService.getFavoriteBlogsByUser(userId);
        return ResponseEntity.ok(favoriteBlogs);
    }



    // ✅ Check if Clapped
    @GetMapping("/{blogId}/clapped/{userId}")
    public ResponseEntity<Boolean> isBlogClappedByUser(@PathVariable Long blogId, @PathVariable Long userId) {
        boolean hasClapped = blogService.isBlogClappedByUser(blogId, userId);
        return ResponseEntity.ok(hasClapped);
    }

    // ✅ Clapped -  Unclapped
    @PostMapping("/{blogId}/clap/{userId}")
    public ResponseEntity<String> clapUnclap(@PathVariable Long blogId, @PathVariable Long userId) {
        return blogService.clapUnclap(blogId, userId);
    }

    // ✅ Claps Count
    @GetMapping("/{blogId}/claps-count")
    public ResponseEntity<Map<String, Integer>> getBlogClapsCount(@PathVariable Long blogId) {
        int clapsCount = blogService.getClapsCount(blogId);
        return ResponseEntity.ok(Collections.singletonMap("count", clapsCount)); // ✅ Return claps as JSON
    }
}