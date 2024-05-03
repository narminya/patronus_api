package com.example.patronus.service;

import com.example.patronus.models.jpa.RefreshToken;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenService {

    RefreshToken save(RefreshToken token);

    List<RefreshToken> saveAll(List<RefreshToken> token);

    RefreshToken findByToken(String token);

    boolean verifyExpiration(RefreshToken token);

    List<RefreshToken> findAllValid(UUID userId);

    RefreshToken getByUser(UUID userId);

    int deleteByUserId(UUID userId);
    int deleteAllByUserId(UUID userId);

}