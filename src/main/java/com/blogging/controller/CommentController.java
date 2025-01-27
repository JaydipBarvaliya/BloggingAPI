package com.blogging.controller;

import com.blogging.DTO.CommentDTO;
import com.blogging.entity.Blog;
import com.blogging.entity.Comment;
import com.blogging.repository.BlogRepository;
import com.blogging.repository.CommentRepository;
import com.blogging.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    private final CommentRepository commentRepository;

    private final BlogRepository blogRepository;

    public CommentController(CommentService commentService, CommentRepository commentRepository, BlogRepository blogRepository) {
        this.commentService = commentService;
        this.commentRepository = commentRepository;
        this.blogRepository = blogRepository;
    }

    @PostMapping
    public ResponseEntity<String> createComment(@RequestBody CommentDTO commentDTO) {

        // Fetch the associated blog using the blogId
        Blog blog = blogRepository.findById(commentDTO.getBlogId())
                .orElseThrow(() -> new RuntimeException("Blog not found with ID: " + commentDTO.getBlogId()));

        // Create and populate the Comment entity
        Comment comment = new Comment();
        comment.setBlog(blog); // Associate the blog
        comment.setContent(commentDTO.getContent());
        comment.setUserId(commentDTO.getUserId());
        comment.setName(commentDTO.getName());

        // Parse ISO-8601 timestamp with 'Z' or offset
        comment.setTimestamp(
                LocalDateTime.parse(commentDTO.getTimestamp(), DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        );

        // Save the comment
        commentRepository.save(comment);

        return ResponseEntity.ok("Comment posted successfully");

    }

    @GetMapping("/{blogId}")
    public ResponseEntity<List<Comment>> getCommentsByBlogId(@PathVariable Long blogId) {
        return ResponseEntity.ok(commentService.getCommentsByBlogId(blogId));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long commentId, @RequestBody CommentDTO commentDTO) {
        return ResponseEntity.ok(commentService.updateComment(commentId, commentDTO));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, @RequestParam Long userId) {
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.noContent().build();
    }
}
