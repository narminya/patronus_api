package com.example.patronus.service;

import com.example.patronus.models.jpa.User;
import com.example.patronus.payload.request.LoginRequest;
import com.example.patronus.payload.request.SignUpRequest;
import com.example.patronus.payload.request.TokenRefreshRequest;
import com.example.patronus.payload.response.AuthResponse;
import com.example.patronus.payload.response.TokenRefreshResponse;

public interface AuthService {

    AuthResponse register(SignUpRequest request);
    AuthResponse authenticate(LoginRequest request);
    String logout(String token);
    TokenRefreshResponse refreshToken(TokenRefreshRequest request);
    void revokeUsersTokens(User user);
}
