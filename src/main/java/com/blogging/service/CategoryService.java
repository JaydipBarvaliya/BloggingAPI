package com.blogging.service;

import com.blogging.entity.Blog;
import com.blogging.exception.ResourceNotFoundException;
import com.blogging.repository.BlogRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final BlogRepository blogRepository;

    public CategoryService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    // Fetch all distinct categories
    public List<String> getAllCategories() {
        return blogRepository.findDistinctCategories();
    }

    // Fetch blogs for a specific category
    @Transactional
    public List<Blog> getBlogsByCategory(String category) {
        List<Blog> blogs = blogRepository.findByCategory(category);
        if (blogs.isEmpty()) {
            throw new ResourceNotFoundException("No blogs found for category: " + category);
        }
        return blogs;
    }
}
