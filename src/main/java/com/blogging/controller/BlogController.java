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
import java.time.LocalDateTime;
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


    @DeleteMapping("/{blogId}")
    public ResponseEntity<String> deleteBlog(@PathVariable Long blogId) {
        try {
            blogService.deleteBlogAndRelatedData(blogId);
            return ResponseEntity.ok("Blog and related data deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to delete blog: " + e.getMessage());
        }
    }



    @PostMapping
    public ResponseEntity<Blog> createBlog(
            @RequestParam("author") String author,
            @RequestParam("category") String category,
            @RequestParam("content") String content,
            @RequestParam("image") MultipartFile image,
            @RequestParam("summary") String summary,
            @RequestParam("title") String title) throws IOException {

        // Convert the uploaded image to byte[] and set it in the blog
        byte[] imageBytes = image.getBytes();

        // Map the parameters to the Blog entity
        Blog blog = new Blog();
        blog.setAuthor(author);
        blog.setCategory(category);
        blog.setImage(imageBytes);  // Set the image byte array
        blog.setSummary(summary);
        blog.setTitle(title);
        blog.setContent(content); // HTML content from the editor
        blog.setPublishedOn(LocalDateTime.now());

        // Save the blog post
        Blog createdBlog = blogService.createBlog(blog);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBlog);
    }


    @GetMapping
    public ResponseEntity<List<BlogDTO>> getAllBlogs() {
        List<BlogDTO> blogDTOs = blogService.getAllBlogs();
        return ResponseEntity.ok(blogDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogDTO> getBlogById(@PathVariable Long id) {
        BlogDTO blogDTO = blogService.getBlogById(id);
        return ResponseEntity.ok(blogDTO);
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