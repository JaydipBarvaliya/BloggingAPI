package com.blogging.service;

import com.blogging.entity.Blog;
import com.blogging.entity.Favorite;
import com.blogging.entity.Userr;
import com.blogging.repository.BlogRepository;
import com.blogging.repository.FavoriteRepository;
import com.blogging.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;


    public FavoriteService(FavoriteRepository favoriteRepository, BlogRepository blogRepository, UserRepository userRepository) {
        this.favoriteRepository = favoriteRepository;
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
    }

    public List<Blog> getFavoritesByUserId(Long userId) {
        List<Long> favoriteBlogIds = favoriteRepository.findBlogIdsByUserrId(userId);
        return blogRepository.findAllById(favoriteBlogIds);
    }

    public void addFavorite(Long userId, Long blogId) {
        Userr userr = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        if (!favoriteRepository.existsByUserrIdAndBlogId(userId, blogId)) {
            Favorite favorite = new Favorite(userr, blogId); // Create a Favorite with Userr and blogId
            favoriteRepository.save(favorite);
        }
    }

    @Transactional
    public void removeFavorite(Long userId, Long blogId) {
        Favorite favorite = favoriteRepository.findByUserrIdAndBlogId(userId, blogId)
                .orElseThrow(() -> new IllegalArgumentException("Favorite not found"));
        favoriteRepository.delete(favorite);
    }
}

