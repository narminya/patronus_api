package com.example.patronus.repository;

import com.example.patronus.models.jpa.Stream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StreamRepository extends JpaRepository<Stream, UUID> {
    @Query(value = "SELECT s.* FROM streams s LEFT JOIN block b ON s.user_id = b.blocker_user_id " +
            "WHERE b.blocked_user_id != :userId OR b.blocked_user_id IS NULL AND s.archived = false", nativeQuery = true)
    Page<Stream> findAll(@Param("userId") UUID userId, Pageable pageable);

    @Query(value = "SELECT * FROM streams WHERE live = true AND user_id = :userId AND archived = false", nativeQuery = true)
    Optional<Stream> findLiveByUserId(UUID userId);

    Optional<Stream> findById(UUID id);

    Page<Stream> findAllByUserId(UUID userId, Pageable pageable);

    @Query(value = "UPDATE streams SET archive = false WHERE id = :id AND live is false AND user_id = :userId", nativeQuery = true)
    Stream updateByStreamIdAndUserId(UUID id, UUID userId);

}
