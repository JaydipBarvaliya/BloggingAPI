package com.blogging.repository;

import com.blogging.entity.Userr;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Userr, Long> {

    Optional<Userr> findByEmail(String email);

    boolean existsByEmail(String email);
}
