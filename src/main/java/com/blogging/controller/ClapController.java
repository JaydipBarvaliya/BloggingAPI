package com.blogging.controller;

import com.blogging.service.BlogService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/claps")
public class ClapController {

    private final BlogService blogService;

    public ClapController(BlogService blogService) {
        this.blogService = blogService;
    }

    // ✅ Check if Clapped
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{blogId}/clapped/{userId}")
    public ResponseEntity<Boolean> isBlogClappedByUser(@PathVariable Long blogId, @PathVariable Long userId) {
        boolean hasClapped = blogService.isBlogClappedByUser(blogId, userId);
        return ResponseEntity.ok(hasClapped);
    }

    // ✅ Clapped -  Unclapped
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping("/{blogId}/clap/{userId}")
    public ResponseEntity<String> clapUnclap(@PathVariable Long blogId, @PathVariable Long userId) {
        return blogService.clapUnclap(blogId, userId);
    }

    // ✅ Claps Count
    @GetMapping("/{blogId}/claps-count")
    public ResponseEntity<Map<String, Integer>> getBlogClapsCount(@PathVariable Long blogId) {
        int clapsCount = blogService.getClapsCount(blogId);
        return ResponseEntity.ok(Collections.singletonMap("count", clapsCount)); // ✅ Return claps as JSON
    }

}
