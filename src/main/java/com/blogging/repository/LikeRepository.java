package com.blogging.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LikeRepository {

    private final JdbcTemplate jdbcTemplate;

    public LikeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int countLikesByBlogId(Long blogId) {
        String sql = "SELECT COUNT(*) FROM likes WHERE blog_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, blogId); // ✅ Fetch total likes
    }
}

