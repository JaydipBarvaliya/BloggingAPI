package com.blogging.controller;

import com.blogging.service.LikesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
public class LikesController {

    private final LikesService likesService;

    public LikesController(LikesService likesService) {
        this.likesService = likesService;
    }

    // Endpoint to get the total likes for a blog
    @GetMapping("/{blogId}")
    public ResponseEntity<Integer> getLikes(@PathVariable Long blogId) {
        int likeCount = likesService.getLikesCount(blogId);
        return ResponseEntity.ok(likeCount);
    }

    // Endpoint to like a blog
    @PostMapping
    public ResponseEntity<String> likeBlog(@RequestParam Long blogId, @RequestParam String userId) {
            likesService.likeBlog(blogId, userId);
            return ResponseEntity.ok("Blog liked successfully.");
    }

    @DeleteMapping
    public ResponseEntity<String> unlikeBlog(@RequestParam Long blogId, @RequestParam String userId) {
            likesService.unlikeBlog(blogId, userId);
            return ResponseEntity.ok("Blog unliked successfully.");
    }

    @GetMapping("/isLiked")
    public ResponseEntity<Boolean> isBlogLikedByUser(@RequestParam Long blogId, @RequestParam String userId) {
        boolean isLiked = likesService.isBlogLikedByUser(blogId, userId);
        return ResponseEntity.ok(isLiked);
    }

}
