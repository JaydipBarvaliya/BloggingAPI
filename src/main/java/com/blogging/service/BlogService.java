package com.blogging.service;

import com.blogging.DTO.BlogDTO;
import com.blogging.entity.AppUser;
import com.blogging.entity.Blog;
import com.blogging.exception.BlogNotFoundException;
import com.blogging.exception.BlogSlugAlreadyExistsException;
import com.blogging.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
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
    private final CommentRepository commentRepository;
    private final FavoriteRepository favoriteRepository;


    public BlogService(BlogRepository blogRepository, UserRepository userRepository, ClapRepository clapRepository, CommentRepository commentRepository, FavoriteRepository favoriteRepository) {
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
        this.clapRepository = clapRepository;
        this.commentRepository = commentRepository;
        this.favoriteRepository = favoriteRepository;
    }


    // Method to generate slug from title
    public String generateSlug(String title) {
        String slug = title.trim().toLowerCase().replaceAll("[^a-z0-9\\s-]", "");
        slug = slug.replaceAll("[\\s-]+", "-"); // Replace multiple spaces or hyphens with a single hyphen
        slug = slug.replaceAll("^-|-$", ""); // Remove leading and trailing hyphens
        return slug;
    }

    @Transactional
    public Blog createBlog(String author, String category, String content, MultipartFile image, String summary, String title) throws IOException {

        // Generate slug before saving
        String slug = generateSlug(title);

        // Check for duplicate slugs
        if (blogRepository.findBySlug(slug) != null) {
            throw new BlogSlugAlreadyExistsException("Slug already exists!");
        }

        // Convert the uploaded image to byte[] and set it in the blog
        byte[] imageBytes = image.getBytes();

        // Map the parameters to the Blog entity
        Blog blog = new Blog();
        blog.setAuthor(author);
        blog.setCategory(category);
        blog.setImage(imageBytes);
        blog.setSummary(summary);
        blog.setTitle(title);
        blog.setContent(content);
        blog.setPublishedOn(LocalDateTime.now());
        blog.setSlug(slug);
        return blogRepository.save(blog);
    }


    @Transactional
    public Blog updateBlog(Long id, String title, String author, String category, String summary, String content, MultipartFile image) throws IOException {
        // Check if the blog exists
        Blog existingBlog = blogRepository.findById(id).orElseThrow(() -> new BlogNotFoundException("Blog not found"));

        // Generate slug for the updated title
        String slug = generateSlug(title);

        // If the slug has changed, check for duplicate slugs
        if (!existingBlog.getSlug().equals(slug) && blogRepository.findBySlug(slug) != null) {
            throw new BlogSlugAlreadyExistsException("Slug already exists!");
        }

        // Update blog fields
        existingBlog.setTitle(title);
        existingBlog.setContent(content);
        existingBlog.setCategory(category);
        existingBlog.setSummary(summary);
        existingBlog.setAuthor(author);
        existingBlog.setSlug(slug);

        // Handle image update if new image is provided
        if (image != null && !image.isEmpty()) {
            byte[] imageBytes = image.getBytes();
            existingBlog.setImage(imageBytes);
        }

        // Save the updated blog
        return blogRepository.save(existingBlog);
    }



    @Transactional
    public List<BlogDTO> getAllBlogs() {
        return blogRepository.findAll().stream()
                .map(blog -> new BlogDTO(
                        blog.getId(),
                        blog.getSlug(),
                        blog.getTitle(),
                        blog.getSummary(),
                        blog.getContent(),
                        blog.getImage(),
                        blog.getAuthor(),
                        blog.getCategory()
                ))
                .collect(Collectors.toList());
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
        return clapRepository.countClapsByBlogId(blogId);
    }

    @Transactional
    public void deleteBlogAndRelatedData(Long blogId) {

        // Delete associated favorites
        favoriteRepository.deleteByBlogId(blogId);

        // Delete comments related to this blog
        commentRepository.deleteByBlogId(blogId);

        // Delete claps related to this blog
        clapRepository.deleteByBlogId(blogId);

        // Delete the blog
        blogRepository.deleteById(blogId);

    }

    @Transactional
    public Blog findBySlug(String slug) {
        return blogRepository.findBySlug(slug);
    }

}