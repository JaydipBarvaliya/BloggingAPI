package com.blogging.controller;

import com.blogging.DTO.BlogDTO;
import com.blogging.DTO.CommentDTO;
import com.blogging.entity.Blog;
import com.blogging.entity.Comment;
import com.blogging.exception.ResourceNotFoundException;
import com.blogging.repository.BlogRepository;
import com.blogging.repository.CommentRepository;
import com.blogging.service.BlogService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {

    private final BlogService blogService;
    private final CommentRepository commentRepository;
    private final BlogRepository blogRepository;

    public BlogController(BlogService blogService, CommentRepository commentRepository, BlogRepository blogRepository) {
        this.blogService = blogService;
        this.commentRepository = commentRepository;
        this.blogRepository = blogRepository;
    }

    @GetMapping
    public ResponseEntity<List<BlogDTO>> getAllBlogs() {
        List<Blog> blogs = blogRepository.findAll();

        // Map Blog entities to BlogDTOs
        List<BlogDTO> blogDTOs = blogs.stream()
                .map(blog -> new BlogDTO(
                        blog.getId(),
                        blog.getTitle(),
                        blog.getSummary(),
                        blog.getContent(),
                        blog.getImage(),
                        blog.getAuthor(),
                        blog.getCategory()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(blogDTOs);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Blog> getBlogById(@PathVariable Long id) {
        Blog blog = blogService.getBlogById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog with ID " + id + " not found"));
        return ResponseEntity.ok(blog);
    }

    @GetMapping(value = "/{id}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommentDTO>> getCommentsByBlogId(@PathVariable Long id) {
        // Fetch comments for the given blog ID
        List<Comment> comments = commentRepository.findByBlogId(id);

        // Map Comment entities to CommentDto objects
        List<CommentDTO> DTOs = comments.stream()
                .map(comment -> new CommentDTO(
                        comment.getId(),
                        comment.getContent(),
                        comment.getUserId(),
                        comment.getName(),
                        comment.getTimestamp().toString() // Convert LocalDateTime to String
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(DTOs);
    }


    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> addCommentToBlog(@PathVariable Long id, @RequestBody Comment comment) {
        Comment createdComment = blogService.addCommentToBlog(id, comment)
                .orElseThrow(() -> new ResourceNotFoundException("Blog with ID " + id + " not found"));
        return ResponseEntity.ok(createdComment);
    }

}