package com.example.booking_train_backend.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AuthenticationResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;        // "Bearer"
    private long expiresIn;          // seconds until expiration
    private boolean authenticated;
}
