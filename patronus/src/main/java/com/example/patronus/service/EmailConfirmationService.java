package com.example.patronus.service;


import com.example.patronus.models.entity.EmailConfirmationToken;

public interface EmailConfirmationService {
     void saveEmailConfirmationToken(EmailConfirmationToken token);
     EmailConfirmationToken getToken(String token);
     int setConfirmedAt(String token);
}