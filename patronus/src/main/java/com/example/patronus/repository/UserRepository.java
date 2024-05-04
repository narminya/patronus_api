package com.example.patronus.repository;

import com.example.patronus.models.jpa.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.emailConfirmed = TRUE WHERE u.email = :email")
    int confirmUserByEmail(@Param("email") String email);
}