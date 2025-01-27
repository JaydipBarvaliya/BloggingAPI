package com.blogging.service;

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
    public Comment updateComment(Long commentId, Comment updatedComment, String userId) {
        Comment comment = commentRepository.findById(updatedComment.getId())
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        if (!comment.getUserId().equals(userId)) {
            throw new SecurityException("You are not authorized to edit this comment");
        }
        comment.setComment(updatedComment.getComment());
        return commentRepository.save(comment);
    }

    // Delete a comment (ensure the user owns the comment)
    public void deleteComment(Long commentId, String userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        if (!comment.getUserId().equals(userId)) {
            throw new SecurityException("You are not authorized to delete this comment");
        }
        commentRepository.delete(comment);
    }
}
