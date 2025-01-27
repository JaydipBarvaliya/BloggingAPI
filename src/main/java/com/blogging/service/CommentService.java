package com.blogging.service;

import com.blogging.DTO.CommentDTO;
import com.blogging.entity.Comment;
import com.blogging.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    // Create a new comment
    public Comment saveComment(Comment comment) {
        comment.setTimestamp(LocalDateTime.now()); // Set timestamp on creation
        return commentRepository.save(comment);
    }

    // Get all comments for a specific blog
    public List<Comment> getCommentsByBlogId(Long blogId) {
        return commentRepository.findByBlogId(blogId);
    }

    // Update a comment (ensure the user owns the comment)
    public Comment updateComment(Long commentId, CommentDTO commentDTO) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        if (!comment.getUserId().equals(commentDTO.getUserId())) {
            throw new SecurityException("You are not authorized to update this comment");
        }

        comment.setContent(commentDTO.getContent());
        comment.setTimestamp(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    // Delete a comment (ensure the user owns the comment)
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        if (!comment.getUserId().equals(userId)) {
            throw new SecurityException("You are not authorized to delete this comment");
        }

        commentRepository.delete(comment);
    }
}
