package com.blogging.service;

import com.blogging.entity.Like;
import com.blogging.exception.ResourceNotFoundException;
import com.blogging.repository.LikeRepository;
import org.springframework.stereotype.Service;

@Service
public class LikesService {

    private final LikeRepository likeRepository;

    public LikesService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    // Like a blog
    public void likeBlog(Long blogId, String userId) {

        if (isBlogLikedByUser(blogId, userId)) {
            throw new IllegalStateException("You have already liked this blog.");
        }

        Like newLike = new Like(blogId, userId); // Assuming Like has a constructor
        likeRepository.save(newLike);
    }

    public void unlikeBlog(Long blogId, String userId) {
        Like existingLike = getExistingLike(blogId, userId);
        likeRepository.delete(existingLike);
    }

    // Get the total likes for a blog
    public int getLikesCount(Long blogId) {
        return likeRepository.countByBlogId(blogId);
    }

    public boolean isBlogLikedByUser(Long blogId, String userId) {
        return likeRepository.findByBlogIdAndUserId(blogId, userId).isPresent();
    }

    private Like getExistingLike(Long blogId, String userId) {
        return likeRepository.findByBlogIdAndUserId(blogId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("You have not liked this blog."));
    }
}
