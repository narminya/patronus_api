package com.example.patronus.service.impl;

import com.example.patronus.models.entity.EmailConfirmationToken;
import com.example.patronus.repository.EmailConfirmationRepository;
import com.example.patronus.service.EmailConfirmationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailConfirmationServiceImpl implements EmailConfirmationService {

    private final EmailConfirmationRepository emailConfirmationTokenRepository;

    public void saveEmailConfirmationToken(EmailConfirmationToken token) {
        emailConfirmationTokenRepository.save(token);
    }

    public EmailConfirmationToken getToken(String token) {
        return emailConfirmationTokenRepository.findByToken(token).orElseThrow();
    }

    public int setConfirmedAt(String token) {
        return emailConfirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
    }
}