package com.example.patronus.service;


import com.example.patronus.models.jpa.EmailConfirmationToken;

public interface EmailConfirmationService {
     void saveEmailConfirmationToken(EmailConfirmationToken token);
     EmailConfirmationToken getToken(String token);
     int setConfirmedAt(String token);
}