package com.example.patronus.service;


import com.example.patronus.base.BaseServiceTest;
import com.example.patronus.enums.UserRole;
import com.example.patronus.exception.user.DuplicatedUserInfoException;
import com.example.patronus.models.entity.Role;
import com.example.patronus.models.entity.User;
import com.example.patronus.payload.request.LoginRequest;
import com.example.patronus.payload.request.SignUpRequest;
import com.example.patronus.payload.response.AuthResponse;
import com.example.patronus.repository.RoleRepository;
import com.example.patronus.repository.TokenRepository;
import com.example.patronus.repository.UserRepository;
import com.example.patronus.security.TokenProvider;
import com.example.patronus.service.impl.AuthServiceImpl;
import com.example.patronus.service.impl.RefreshTokenServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class AuthServiceImplTest extends BaseServiceTest {

    @InjectMocks
    AuthServiceImpl authService;
    @Mock
    EmailConfirmationService emailConfirmationService;
    @Mock
    EmailService emailService;
    @Mock
    RefreshTokenServiceImpl tokenService;
    @Mock
    TokenRepository tokenRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    TokenProvider provider;

    @Mock
    private AuthenticationManager authenticationManager;

    private static Role role;
    private static  SignUpRequest signUpRequest;
    private static LoginRequest loginRequest;

    @BeforeAll
    static void setup() {
        role = Role.builder()
                .name(UserRole.valueOf("USER"))
                .id(UUID.randomUUID())
                .build();

        signUpRequest = SignUpRequest.builder()
                .email("test@gmail.com")
                .username("test123")
                .fullName("testName")
                .password("123456789")
                .build();

        loginRequest = LoginRequest.builder()
                .email("test@gmail.com")
                .password("123456789")
                .build();
    }

    @Test
    void registerUserSuccessful_ReturnTokens() {
        //arrange - given
           User user = User.builder()
                .email(signUpRequest.getEmail())
                .name(signUpRequest.getFullName())
                .username(signUpRequest.getUsername())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .roles(new HashSet<>())
                .build();
        user.getRoles().add(role);

        Authentication mockAuthentication = new UsernamePasswordAuthenticationToken(
                signUpRequest.getEmail(), signUpRequest.getPassword());

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);

        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(signUpRequest.getUsername())).thenReturn(false);
        when(provider.generate(mockAuthentication)).thenReturn("mockAccessToken");
        when(provider.generateRefreshToken(mockAuthentication)).thenReturn("mockRefreshToken");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(roleRepository.findByName(any())).thenReturn(Optional.ofNullable(role));

        AuthResponse response = authService.register(signUpRequest);

        AuthResponse expected = AuthResponse.builder()
                .accessToken("mockAccessToken")
                .refreshToken("mockRefreshToken")
                .build();

        assertEquals(expected, response);
        assertEquals("mockAccessToken", response.getAccessToken());
        assertEquals("mockRefreshToken", response.getRefreshToken());

        verify(roleRepository).findByName(UserRole.valueOf("USER"));
        verify(userRepository).save(any(User.class));
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(provider).generate(mockAuthentication);
        verify(provider).generateRefreshToken(mockAuthentication);

    }

    @Test
    void registerDuplicateUsername_ThrowsException(){
        when(userRepository.existsByEmail(signUpRequest.getUsername())).thenReturn(true);
        assertThrowsExactly(DuplicatedUserInfoException.class, () -> authService.register(signUpRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerDuplicateEmail_ThrowsException(){
        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(true);
        assertThrowsExactly(DuplicatedUserInfoException.class, () -> authService.register(signUpRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void authenticateUser_ReturnsTokens(){
        Authentication mockAuthentication = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), signUpRequest.getPassword());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
        when(provider.generate(mockAuthentication)).thenReturn("mockAccessToken");
        when(provider.generateRefreshToken(mockAuthentication)).thenReturn("mockRefreshToken");

        AuthResponse response = authService.authenticate(loginRequest);
        AuthResponse expected = AuthResponse.builder()
                .accessToken("mockAccessToken")
                .refreshToken("mockRefreshToken")
                .email(loginRequest.getEmail())
                .build();

        assertEquals(expected, response);
        assertEquals("mockAccessToken", response.getAccessToken());
        assertEquals("mockRefreshToken", response.getRefreshToken());

        verify(provider).generate(mockAuthentication);
        verify(provider).generateRefreshToken(mockAuthentication);
    }

}
