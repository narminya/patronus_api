package com.example.patronus.service.impl;

import com.example.patronus.exception.UserNotFoundException;
import com.example.patronus.exception.token.TokenNotFoundException;
import com.example.patronus.exception.token.TokenRefreshException;
import com.example.patronus.models.jpa.RefreshToken;
import com.example.patronus.models.jpa.User;
import com.example.patronus.repository.TokenRepository;
import com.example.patronus.service.RefreshTokenService;
import com.example.patronus.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final TokenRepository tokenRepository;

    private final UserService userService;
    public boolean verifyExpiration(RefreshToken refreshToken) {
        RefreshToken token = tokenRepository.findByToken(refreshToken.getToken())
                .orElseThrow(TokenNotFoundException::new);
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            token.setRevoked(true);
            token.setExpired(true);
            tokenRepository.save(token);
            throw new TokenRefreshException(token.getToken(), "Token ");
        }
        return true;
    }

    @Override
    public List<RefreshToken> findAllValid(UUID userId) {
       return tokenRepository.findAllValidTokenByUser(userId);
    }

    @Override
    public RefreshToken getByUser(UUID userId) {
        return tokenRepository.findByUserId(userId)
                .orElseThrow(TokenNotFoundException::new);
    }


    @Override
    public RefreshToken save(RefreshToken token) {
       return tokenRepository.save(token);
    }
    @Override
    public List<RefreshToken> saveAll(List<RefreshToken> token) {
        return tokenRepository.saveAll(token);
    }
    @Override
    public RefreshToken findByToken(String token) {
        return tokenRepository.findByToken(token)
                .orElseThrow(TokenNotFoundException::new);
    }


    @Override
    @Transactional
    public int deleteByUserId(UUID userId) {
        User user = userService.getUserById(userId);
        return tokenRepository.deleteByUser(user);
    }

    @Override
    public int deleteAllByUserId(UUID userId) {
        User user = userService.getUserById(userId);
        return tokenRepository.revokeAllByUser(user);
    }
}
