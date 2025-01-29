package com.blogging.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final JdbcTemplate jdbcTemplate;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    public DataInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeData() {
        if ("create-drop".equalsIgnoreCase(ddlAuto) || "create".equalsIgnoreCase(ddlAuto)) {
            insertTestData();
        }
    }

    private void insertTestData() {
        // Insert users into 'app_user' (renamed from 'userr')
        jdbcTemplate.execute("""
            INSERT INTO app_user (email, password, first_name, last_name)
            SELECT CONCAT('user', n, '@example.com') AS email, 
                   CONCAT('password', n) AS password,
                   CONCAT('First', n) AS first_name,
                   CONCAT('Last', n) AS last_name
            FROM generate_series(1, 100) AS n;
        """);

        // Insert blogs
        jdbcTemplate.execute("""
            INSERT INTO blog (author, category, content, image, summary, title)
            SELECT CONCAT('Author ', n) AS author, 
                   CASE WHEN n % 5 = 0 THEN 'Technology'
                        WHEN n % 5 = 1 THEN 'Finance'
                        WHEN n % 5 = 2 THEN 'Health'
                        WHEN n % 5 = 3 THEN 'Lifestyle'
                        ELSE 'Education'
                   END AS category,
                   CONCAT('This is the content of blog ', n) AS content,
                   CONCAT('https://picsum.photos/800/400?random=', n) AS image,
                   CONCAT('This is a summary for blog ', n) AS summary,
                   CONCAT('Blog Title ', n) AS title
            FROM generate_series(1, 200) AS n;
        """);

        // Insert comments (Fixed column name: `app_user_id` instead of `user_id`)
        jdbcTemplate.execute("""
            INSERT INTO comment (blog_id, content, name, app_user_id, timestamp)
            SELECT blog_id,
                   CONCAT('This is a comment for blog ', blog_id) AS content,
                   CONCAT('User ', FLOOR(1 + (RANDOM() * 100))) AS name,
                   FLOOR(1 + (RANDOM() * 100)) AS app_user_id, 
                   NOW() AS timestamp
            FROM (
                SELECT id AS blog_id
                FROM blog
                ORDER BY RANDOM()
                LIMIT 500
            ) AS valid_blogs;
        """);

        // Insert favorites (handling ManyToMany relationship with correct column names)
        jdbcTemplate.execute("""
            INSERT INTO favorite (app_user_id, blog_id)
            SELECT user_id, blog_id
            FROM (
                SELECT u.id AS user_id, b.id AS blog_id
                FROM app_user u
                CROSS JOIN blog b
                ORDER BY RANDOM()
                LIMIT 500
            ) AS valid_data;
        """);

        System.out.println("✅ Test data inserted successfully.");
    }
}
