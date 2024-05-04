package com.example.patronus.service.impl;



import com.example.patronus.exception.user.UserNotFoundException;
import com.example.patronus.models.jpa.User;
import com.example.patronus.repository.UserRepository;
import com.example.patronus.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

    }

    @Override
    public User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }
    @Override
    public boolean hasUserWithUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    @Override
    public boolean hasUserWithEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
