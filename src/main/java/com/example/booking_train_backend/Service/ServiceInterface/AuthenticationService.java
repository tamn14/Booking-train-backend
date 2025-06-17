package com.example.booking_train_backend.Service.ServiceInterface;

import com.example.booking_train_backend.DTO.Request.LoginRequest;
import com.example.booking_train_backend.DTO.Request.LogoutRequest;
import com.example.booking_train_backend.DTO.Request.RefreshRequest;
import com.example.booking_train_backend.DTO.Response.AuthenticationResponse;
import com.example.booking_train_backend.Model.Auth.TokenInfo;

public interface AuthenticationService {
    public String getAccessToken (LoginRequest loginRequest) ;
    public AuthenticationResponse login (LoginRequest loginRequest) ;
    public AuthenticationResponse refreshToken (RefreshRequest request) ;
//    public String getKeycloakUserName(String email) ;
    public void logout(LogoutRequest logoutRequest) ;
}
