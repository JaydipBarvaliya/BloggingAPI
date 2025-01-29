package com.blogging.service;

import com.blogging.DTO.BlogDTO;
import com.blogging.entity.AppUser;
import com.blogging.entity.Blog;
import com.blogging.exception.ResourceNotFoundException;
import com.blogging.repository.BlogRepository;
import com.blogging.repository.ClapRepository;
import com.blogging.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final ClapRepository clapRepository;


    public BlogService(BlogRepository blogRepository, UserRepository userRepository, ClapRepository clapRepository) {
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
        this.clapRepository = clapRepository;
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

    // ✅ Toggle Clap
    @Transactional
    public ResponseEntity<String> clapUnclap(Long blogId, Long userId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new IllegalArgumentException("Blog not found"));

        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getClappedBlogs().contains(blog)) {
            user.getClappedBlogs().remove(blog);
            userRepository.save(user);
            return ResponseEntity.ok("Unclapped successfully");

        } else {
            user.getClappedBlogs().add(blog);
            userRepository.save(user);
            return ResponseEntity.ok("Clapped successfully");
        }


    }

    // ✅ Toggle Favorite
    @Transactional
    public void toggleFavorite(Long blogId, Long userId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new IllegalArgumentException("Blog not found"));

        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getFavoritedBlogs().contains(blog)) {
            user.getFavoritedBlogs().remove(blog);
        } else {
            user.getFavoritedBlogs().add(blog);
        }

        userRepository.save(user);
    }

    // ✅ Check if User Clapped Blog
    public boolean isBlogClappedByUser(Long blogId, Long userId) {
        return userRepository.isBlogClappedByUser(userId, blogId);
    }

    // ✅ Check if User Favorited Blog
    public boolean isBlogFavoritedByUser(Long blogId, Long userId) {
        return userRepository.existsByIdAndFavoritedBlogs_Id(userId, blogId);
    }

    public List<Blog> getFavoriteBlogsByUser(Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Force Hibernate to fetch favorite blogs
        Set<Blog> favoriteBlogs = new HashSet<>(user.getFavoritedBlogs()); // Fetch first

        // Convert to List before returning
        return new ArrayList<>(favoriteBlogs);
    }

    public int getClapsCount(Long blogId) {
        return clapRepository.countClapsByBlogId(blogId); // ✅ Call repository method
    }
}