package com.example.booking_train_backend.Service.ServiceInterface;

public interface TokenBlacklistService {
    public  void addToBlacklist(String token);
    public boolean isBlacklisted(String token);
}
