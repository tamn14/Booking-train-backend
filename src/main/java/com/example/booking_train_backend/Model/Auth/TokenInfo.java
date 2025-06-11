package com.example.booking_train_backend.Model.Auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
@Setter
@Getter
@AllArgsConstructor @NoArgsConstructor
public class TokenInfo {
    private String cachedToken ;  // luu tru token user dang nhap nhan
    private Instant tokenExpiry ; // thoi diem het han cua token
    private String refreshToken ; // luu tru refresh token tuong ung voi access token
    private Instant refreshTokenExpiry ; // thoi diem refresh token het han
}
