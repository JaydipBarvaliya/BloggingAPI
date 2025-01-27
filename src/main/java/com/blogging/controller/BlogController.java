package com.blogging.controller;

import com.blogging.DTO.BlogDTO;
import com.blogging.DTO.CommentDTO;
import com.blogging.service.BlogService;
import com.blogging.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {

    private final BlogService blogService;
    private final CommentService commentService;

    public BlogController(BlogService blogService, CommentService commentService) {
        this.blogService = blogService;
        this.commentService = commentService;
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

}