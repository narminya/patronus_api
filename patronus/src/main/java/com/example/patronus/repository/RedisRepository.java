package com.example.patronus.repository;


import com.example.patronus.models.redis.StreamHash;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RedisRepository extends CrudRepository<StreamHash, UUID> {
    Optional<StreamHash> findByUserId(UUID userId);
    List<StreamHash> findAll(Pageable pageable);

}