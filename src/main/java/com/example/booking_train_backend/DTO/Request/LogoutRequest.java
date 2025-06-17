package com.example.booking_train_backend.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogoutRequest {
    private String accessToken;   // token đang được dùng để gọi API
    private String refreshToken;  // token để revoke
}
