package com.blogging.service;

import com.blogging.DTO.CommentDTO;
import com.blogging.entity.Blog;
import com.blogging.entity.Comment;
import com.blogging.exception.BlogNotFoundException;
import com.blogging.exception.ResourceNotFoundException;
import com.blogging.repository.BlogRepository;
import com.blogging.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BlogRepository blogRepository;

    public CommentService(CommentRepository commentRepository, BlogRepository blogRepository) {
        this.commentRepository = commentRepository;
        this.blogRepository = blogRepository;
    }

    public void createComment(CommentDTO commentDTO) {
        // Fetch the associated blog using the blogId
        Blog blog = blogRepository.findById(commentDTO.getBlogId())
                .orElseThrow(() -> new BlogNotFoundException("Blog not found with ID: " + commentDTO.getBlogId()));

        // Create and populate the Comment entity
        Comment updatedComment = buildCommentFromDTO(commentDTO, blog);

        // Save the comment
        commentRepository.save(updatedComment);
    }

    // Create a new comment
    public Comment saveComment(Comment comment) {
        comment.setTimestamp(LocalDateTime.now()); // Set timestamp on creation
        return commentRepository.save(comment);
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


    public List<CommentDTO> getCommentsByBlogId(Long blogId) {
        return commentRepository.findByBlogId(blogId).stream()
                .map(comment -> new CommentDTO(
                        comment.getId(),
                        comment.getContent(),
                        comment.getUserId(),
                        comment.getName(),
                        comment.getTimestamp().toString()
                ))
                .collect(Collectors.toList());
    }

    public CommentDTO addCommentToBlog(Long blogId, CommentDTO commentDTO) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new ResourceNotFoundException("Blog with ID " + blogId + " not found"));

        Comment updatedComment = buildCommentFromDTO(commentDTO, blog);

        Comment savedComment = commentRepository.save(updatedComment);
        return new CommentDTO(
                savedComment.getId(),
                savedComment.getContent(),
                savedComment.getUserId(),
                savedComment.getName(),
                savedComment.getTimestamp().toString()
        );
    }

    private Comment buildCommentFromDTO(CommentDTO commentDTO, Blog blog) {
        Comment comment = new Comment();
        comment.setBlog(blog);
        comment.setContent(commentDTO.getContent());
        comment.setUserId(commentDTO.getUserId());
        comment.setName(commentDTO.getName());
        comment.setTimestamp(LocalDateTime.parse(commentDTO.getTimestamp(), DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        return comment;
    }


}
