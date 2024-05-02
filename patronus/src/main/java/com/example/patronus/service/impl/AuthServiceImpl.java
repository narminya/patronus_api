package com.example.patronus.service.impl;

import com.example.patronus.enums.ERole;
import com.example.patronus.exception.DuplicatedUserInfoException;
import com.example.patronus.models.jpa.RefreshToken;
import com.example.patronus.models.jpa.Role;
import com.example.patronus.models.jpa.User;
import com.example.patronus.payload.request.LoginRequest;
import com.example.patronus.payload.request.SignUpRequest;
import com.example.patronus.payload.request.TokenRefreshRequest;
import com.example.patronus.payload.response.AuthResponse;
import com.example.patronus.payload.response.TokenRefreshResponse;
import com.example.patronus.repository.RoleRepository;
import com.example.patronus.repository.TokenRepository;
import com.example.patronus.repository.UserRepository;
import com.example.patronus.security.TokenProvider;
import com.example.patronus.service.AuthService;
import com.example.patronus.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Value("${app.jwtExpirationMs}")
    private Long jwtExpirationMs;
    @Value("${app.jwtRefreshExpirationMs}")
    private Long jwtRefreshExpirationMs;


    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(SignUpRequest request) {

        if (hasUserWithUsername(request.getUsername())) {
            throw new DuplicatedUserInfoException(String.format("Username %s already been used", request.getUsername()));
        }
        if (hasUserWithEmail(request.getEmail())) {
            throw new DuplicatedUserInfoException(String.format("Email %s already been used", request.getEmail()));
        }
        Role optionalRole = roleRepository
                .findByName(ERole.valueOf("USER")).orElseThrow();

        User user = User.builder()
                .email(request.getEmail())
                .bio(request.getFullName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        user.getRoles().add(optionalRole);

        var jwtToken = generateToken(request.getUsername(), request.getPassword());
        var refreshToken = generateRefreshToken(request.getUsername(), request.getPassword());
        saveUserToken(user, refreshToken);

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthResponse authenticate(LoginRequest request) {
        var jwtToken = generateToken(request.getEmail(), request.getPassword());
        var refreshToken = generateRefreshToken(request.getEmail(), request.getPassword());

        AuthResponse response = new AuthResponse();
        response.setAccessToken(jwtToken);
        response.setRefreshToken(refreshToken);


        return AuthResponse.builder()
                .email(request.getEmail())
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public String logout(String token) {
        return null;
    }

    @Override
    public TokenRefreshResponse refreshToken(TokenRefreshRequest request) {
        final String refreshToken;
        final String username;

        return null;
    }

    public void revokeUsersTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = RefreshToken.builder()
                .user(user)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .expiryDate(Instant.now().plusSeconds(jwtRefreshExpirationMs))
                .build();
        tokenRepository.save(token);
    }

    public boolean hasUserWithUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean hasUserWithEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private String generateToken(
            String username, String password
    ) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return tokenProvider.generate(authentication);
    }


    private String generateRefreshToken(
            String username, String password
    ) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return tokenProvider.generate(authentication);
    }
}
