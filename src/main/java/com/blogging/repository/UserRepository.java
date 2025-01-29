package com.blogging.repository;

import com.blogging.entity.AppUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    @EntityGraph(attributePaths = "favoritedBlogs")
    Optional<AppUser> findById(Long id);

    Optional<AppUser> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT COUNT(u) > 0 FROM AppUser u JOIN u.clappedBlogs lb WHERE u.id = :userId AND lb.id = :blogId")
    boolean isBlogClappedByUser(@Param("userId") Long userId, @Param("blogId") Long blogId);

    @Query("SELECT COUNT(u) > 0 FROM AppUser u JOIN u.favoritedBlogs fb WHERE u.id = :userId AND fb.id = :blogId")
    boolean existsByIdAndFavoritedBlogs_Id(@Param("userId") Long userId, @Param("blogId") Long blogId);
}
