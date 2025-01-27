package com.blogging.service;

import com.blogging.entity.Like;
import com.blogging.repository.LikeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikesService {

    private final LikeRepository likeRepository;

    public LikesService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    // Like a blog
    public void likeBlog(Long blogId, String userId) {
        // Check if the user has already liked the blog
        Optional<Like> existingLike = likeRepository.findByBlogIdAndUserId(blogId, userId);

        if (existingLike.isPresent()) {
            throw new IllegalStateException("You have already liked this blog.");
        }

        Like newLike = new Like();
        newLike.setBlogId(blogId);
        newLike.setUserId(userId);

        likeRepository.save(newLike);
    }

    // Get the total likes for a blog
    public int getLikesCount(Long blogId) {
        return likeRepository.countByBlogId(blogId);
    }

    public void unlikeBlog(Long blogId, String userId) {
        Optional<Like> existingLike = likeRepository.findByBlogIdAndUserId(blogId, userId);

        if (existingLike.isEmpty()) {
            throw new IllegalStateException("You have not liked this blog.");
        }

        likeRepository.delete(existingLike.get());
    }

    public boolean isBlogLikedByUser(Long blogId, String userId) {
        return likeRepository.findByBlogIdAndUserId(blogId, userId).isPresent();
    }
}
