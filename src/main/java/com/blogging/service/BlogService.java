package com.blogging.service;

import com.blogging.entity.Blog;
import com.blogging.entity.Comment;
import com.blogging.repository.BlogRepository;
import com.blogging.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogService {

    private final BlogRepository blogRepository;
    private final CommentRepository commentRepository;

    public BlogService(BlogRepository blogRepository, CommentRepository commentRepository) {
        this.blogRepository = blogRepository;
        this.commentRepository = commentRepository;
    }

    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    public Optional<Blog> getBlogById(Long id) {
        return blogRepository.findById(id);
    }

    public List<Comment> getCommentsByBlogId(Long blogId) {
        return commentRepository.findByBlogId(blogId);
    }

    public Optional<Comment> addCommentToBlog(Long blogId, Comment comment) {
        return blogRepository.findById(blogId).map(blog -> {
            comment.setBlog(blog);
            return commentRepository.save(comment);
        });
    }
}