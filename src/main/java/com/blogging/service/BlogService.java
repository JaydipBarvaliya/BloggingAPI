package com.blogging.service;

import com.blogging.DTO.BlogDTO;
import com.blogging.entity.Blog;
import com.blogging.entity.Comment;
import com.blogging.exception.ResourceNotFoundException;
import com.blogging.repository.BlogRepository;
import com.blogging.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BlogService {

    private final BlogRepository blogRepository;
    private final CommentRepository commentRepository;

    public BlogService(BlogRepository blogRepository, CommentRepository commentRepository) {
        this.blogRepository = blogRepository;
        this.commentRepository = commentRepository;
    }



    public List<BlogDTO> getAllBlogs() {
        return blogRepository.findAll().stream()
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
    }

    public BlogDTO getBlogById(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog with ID " + id + " not found"));
        return new BlogDTO(
                blog.getId(),
                blog.getTitle(),
                blog.getSummary(),
                blog.getContent(),
                blog.getImage(),
                blog.getAuthor(),
                blog.getCategory()
        );
    }

    public List<Comment> getCommentsByBlogId(Long blogId) {
        return commentRepository.findByBlogId(blogId);
    }

    public Optional<Comment> addCommentToBlog(Long blogId, Comment comment) {
        return blogRepository.findById(blogId).map(blog -> {
            comment.setId(blogId);
            return commentRepository.save(comment);
        });
    }
}