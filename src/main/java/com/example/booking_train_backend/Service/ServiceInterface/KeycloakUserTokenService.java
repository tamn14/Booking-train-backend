package com.example.booking_train_backend.Service.ServiceInterface;

import com.example.booking_train_backend.DTO.Request.LoginRequest;
import com.example.booking_train_backend.Model.Auth.TokenInfo;

public interface KeycloakUserTokenService {
    public  String getAccessToken (LoginRequest loginRequest) ;
    public TokenInfo refreshToken (LoginRequest loginRequest) ;
//    public String getKeycloakUserName(String email) ;
}
