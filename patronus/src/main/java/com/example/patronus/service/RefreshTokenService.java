package com.example.patronus.service;

import com.example.patronus.models.jpa.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenService {

    RefreshToken findByToken(String token);

    boolean verifyExpiration(RefreshToken token);

    RefreshToken getByUser(UUID userId);

    int deleteByUserId(UUID userId);
}