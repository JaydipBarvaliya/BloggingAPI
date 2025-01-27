package com.blogging.controller;

import com.blogging.DTO.CommentDTO;
import com.blogging.entity.Comment;
import com.blogging.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<String> createComment(@RequestBody CommentDTO commentDTO) {
        commentService.createComment(commentDTO);
        return ResponseEntity.ok("Comment posted successfully");
    }

    @GetMapping("/{blogId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByBlogId(@PathVariable Long blogId) {
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
