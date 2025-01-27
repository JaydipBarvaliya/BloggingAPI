package com.blogging.repository;

import com.blogging.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByBlogIdAndUserId(Long blogId, String userId);

    int countByBlogId(Long blogId);
}
