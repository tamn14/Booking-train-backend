package com.example.booking_train_backend.Service.ServiceInterface;


public interface KeycloakClientTokenService {
    public  String getAccessToken () ;
    // phuong thuc refresh token cho client (client dong vai tro la he thong khong phai user dang dang nhap)
    public void refreshToken() ;
}
