package com.example.patronus.service;

import com.example.patronus.models.jpa.User;

import java.util.UUID;

public interface UserService {
    User save(User user);
    User getUser(String username);
    User getUserById(UUID userId);
    boolean hasUserWithUsername(String username);
    boolean hasUserWithEmail(String email);
}
