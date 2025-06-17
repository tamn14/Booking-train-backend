package com.example.booking_train_backend.Controller;

import com.example.booking_train_backend.DTO.Request.LoginRequest;
import com.example.booking_train_backend.DTO.Request.LogoutRequest;
import com.example.booking_train_backend.DTO.Request.RefreshRequest;
import com.example.booking_train_backend.DTO.Response.ApiResponse;
import com.example.booking_train_backend.DTO.Response.AuthenticationResponse;
import com.example.booking_train_backend.Service.ServiceInterface.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j

public class AuthenticationController {
    private AuthenticationService authenticationService ;
    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        AuthenticationResponse loginResponse = authenticationService.login(loginRequest) ;
        return ApiResponse.<AuthenticationResponse>builder()
                .mess("Success")
                .result(loginResponse)
                .build();
    }
    @PostMapping("/refressToken")
    public ApiResponse<AuthenticationResponse> refresToken(@RequestBody @Valid RefreshRequest request) {
        AuthenticationResponse loginResponse = authenticationService.refreshToken(request) ;
        return ApiResponse.<AuthenticationResponse>builder()
                .mess("Success")
                .result(loginResponse)
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> refresToken(@RequestBody @Valid LogoutRequest request) {
        authenticationService.logout(request) ;
        return ApiResponse.<Void>builder()
                .mess("Success")
                .build();
    }










}
