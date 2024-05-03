package com.example.patronus.service.impl;

import com.example.patronus.enums.ERole;
import com.example.patronus.exception.DuplicatedUserInfoException;
import com.example.patronus.models.jpa.RefreshToken;
import com.example.patronus.models.jpa.Role;
import com.example.patronus.models.jpa.User;
import com.example.patronus.payload.request.LoginRequest;
import com.example.patronus.payload.request.SignUpRequest;
import com.example.patronus.payload.response.AuthResponse;
import com.example.patronus.payload.response.TokenRefreshResponse;
import com.example.patronus.repository.RoleRepository;
import com.example.patronus.repository.TokenRepository;
import com.example.patronus.security.TokenProvider;
import com.example.patronus.service.AuthService;
import com.example.patronus.service.RefreshTokenService;
import com.example.patronus.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Value("${app.jwtRefreshExpirationMs}")
    private Long jwtRefreshExpirationMs;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService tokenService;

    @Override
    public AuthResponse register(SignUpRequest request) {

        if (userService.hasUserWithUsername(request.getUsername())) {
            throw new DuplicatedUserInfoException(String.format("Username %s already been used", request.getUsername()));
        }
        if (userService.hasUserWithEmail(request.getEmail())) {
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
    public void logout(String token) {
        String refreshToken = extractTokenFromHeader(token);
        RefreshToken existing = tokenService.findByToken(refreshToken);
        User user = userService.getUser(existing.getUser().getUsername());
        tokenService.deleteAllByUserId(user.getId());
    }

    @Override
    public TokenRefreshResponse refreshToken(String token) {
        String refreshToken = extractTokenFromHeader(token);
        RefreshToken existing = tokenService.findByToken(refreshToken);
        if (!tokenService.verifyExpiration(existing)) {

            var newToken = generateToken(existing.getUser().getUsername(),
                    existing.getUser().getPassword());
            return TokenRefreshResponse.builder()
                    .accessToken(newToken)
                    .refreshToken(existing.getToken())
                    .build();
        }
        return null;
    }

    public void revokeUsersTokens(User user) {
        var validUserTokens = tokenService.findAllValid(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenService.saveAll(validUserTokens);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = RefreshToken.builder()
                .user(user)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .expiryDate(Instant.now().plusSeconds(jwtRefreshExpirationMs))
                .build();
        tokenService.save(token);
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

    private String extractTokenFromHeader(String authorizationHeader) {

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

}
