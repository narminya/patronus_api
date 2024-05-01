package com.example.patronus.service;

import com.example.patronus.models.jpa.User;

import java.util.UUID;

public interface UserService {
    User getUser(String username);
    User getUserById(UUID userId);
}
