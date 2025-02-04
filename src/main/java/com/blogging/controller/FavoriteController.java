package com.blogging.controller;

import com.blogging.entity.Blog;
import com.blogging.service.BlogService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final BlogService blogService;

    public FavoriteController(BlogService blogService) {
        this.blogService = blogService;
    }

    // ✅ Toggle Favorite
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping("/{blogId}/{userId}")
    public ResponseEntity<String> toggleFavorite(@PathVariable Long blogId, @PathVariable Long userId) {
        blogService.toggleFavorite(blogId, userId);
        return ResponseEntity.ok("Favorite toggled successfully");
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{userId}")
    public ResponseEntity<List<Blog>> getUserFavorites(@PathVariable Long userId) {
        List<Blog> favoriteBlogs = blogService.getFavoriteBlogsByUser(userId);
        return ResponseEntity.ok(favoriteBlogs);
    }

}
