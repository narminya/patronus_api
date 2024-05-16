package com.example.patronus.controllers;

import com.example.patronus.payload.request.LoginRequest;
import com.example.patronus.payload.request.SignUpRequest;
import com.example.patronus.payload.response.AuthResponse;
import com.example.patronus.payload.response.TokenRefreshResponse;
import com.example.patronus.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Registers a new user and returns a success message.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User registered successfully",
                            content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
            })
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody SignUpRequest request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }
    @PostMapping("/logout")
    @Operation(summary = "Logout user", description = "Logs out a user by invalidating the authentication token.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User logged out successfully",
                            content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
            },
            security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Logs in a user and returns JWT tokens.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User logged in successfully",
                            content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
            })
    public ResponseEntity<AuthResponse> authenticate(
            @Valid @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh token", description = "Refreshes the JWT authentication token using a refresh token.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token refreshed successfully",
                            content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
            })
    public ResponseEntity<TokenRefreshResponse> refreshToken(
            @Valid @RequestHeader("Authorization") String token
    ) {
        return ResponseEntity.ok(authService.refreshToken(token));
    }


    @PostMapping(path = "/confirm")
    public ResponseEntity<String> confirm(@RequestParam("emailToken") String emailToken) {
        return ResponseEntity.ok(authService.confirmToken(emailToken));
    }
}
