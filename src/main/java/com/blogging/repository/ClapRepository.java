package com.blogging.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ClapRepository  {

    private final JdbcTemplate jdbcTemplate;

    public ClapRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int countClapsByBlogId(Long blogId) {
        String sql = "SELECT COUNT(*) FROM claps WHERE blog_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, blogId); // ✅ Fetch total claps
    }


    public void deleteByBlogId(Long blogId) {
        String sql = "DELETE FROM claps WHERE blog_id = ?";
        jdbcTemplate.update(sql, blogId);  // Execute the delete query
    }
}

