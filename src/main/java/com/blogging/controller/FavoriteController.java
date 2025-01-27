package com.blogging.controller;

import com.blogging.entity.Blog;
import com.blogging.service.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public ResponseEntity<List<Blog>> getFavorites(@RequestHeader("userId") Long userId) {
        List<Blog> favoriteBlogs = favoriteService.getFavoritesByUserId(userId);
        return ResponseEntity.ok(favoriteBlogs);
    }

    @PostMapping("/{blogId}")
    public ResponseEntity<?> addFavorite(@RequestHeader("userId") Long userId, @PathVariable Long blogId) {
        favoriteService.addFavorite(userId, blogId);
        return ResponseEntity.ok("Blog added to favorites");
    }

    @DeleteMapping("/{blogId}")
    public ResponseEntity<?> removeFavorite(@RequestHeader("userId") Long userId, @PathVariable Long blogId) {
        favoriteService.removeFavorite(userId, blogId);
        return ResponseEntity.ok("Blog removed from favorites");
    }
}

