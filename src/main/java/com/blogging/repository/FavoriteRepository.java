package com.blogging.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FavoriteRepository {

    private final JdbcTemplate jdbcTemplate;

    public FavoriteRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Method to delete favorites by blogId using native SQL query
    public void deleteByBlogId(Long blogId) {
        String sql = "DELETE FROM favorite WHERE blog_id = ?";
        jdbcTemplate.update(sql, blogId); // Delete all favorites associated with the given blogId
    }
}
