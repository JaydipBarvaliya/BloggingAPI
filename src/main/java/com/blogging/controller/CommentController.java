package com.blogging.controller;

import com.blogging.entity.Comment;
import com.blogging.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment, @AuthenticationPrincipal UserDetails userDetails) {
        comment.setUserId(userDetails.getUsername());
        comment.setTimestamp(LocalDateTime.now());
        return ResponseEntity.ok(commentService.saveComment(comment));
    }

    @GetMapping("/{blogId}")
    public ResponseEntity<List<Comment>> getCommentsByBlogId(@PathVariable Long blogId) {
        return ResponseEntity.ok(commentService.getCommentsByBlogId(blogId));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long commentId, @RequestBody Comment comment, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(commentService.updateComment(commentId, comment, userDetails.getUsername()));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetails userDetails) {
        commentService.deleteComment(commentId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
