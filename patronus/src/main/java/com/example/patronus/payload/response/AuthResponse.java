package com.example.patronus.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
        private String accessToken;

        @Builder.Default
        private String type = "Bearer";
        private String refreshToken;
        private String email;


}
