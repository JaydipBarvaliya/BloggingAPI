package com.blogging.repository;

import com.blogging.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    @Query("SELECT f.blogId FROM Favorite f WHERE f.userr.id = :userId")
    List<Long> findBlogIdsByUserrId(@Param("userId") Long userId);

    boolean existsByUserrIdAndBlogId(Long userId, Long blogId);

    Optional<Favorite> findByUserrIdAndBlogId(Long userId, Long blogId);
}
