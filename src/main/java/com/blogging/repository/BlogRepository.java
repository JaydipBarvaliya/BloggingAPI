package com.blogging.repository;

import com.blogging.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

    List<Blog> findByCategory(String category);

    @Query("SELECT DISTINCT b.category FROM Blog b")
    List<String> findDistinctCategories();

    Blog findBySlug(String slug);
}