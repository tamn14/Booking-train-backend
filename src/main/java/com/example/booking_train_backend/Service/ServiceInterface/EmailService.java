package com.example.booking_train_backend.Service.ServiceInterface;


import com.example.booking_train_backend.Entity.Booking;
import com.example.booking_train_backend.Entity.Users;

public interface EmailService {
    public void SendMessage(String from, String to  , byte[] qrBytes , Users passenger , Booking booking) ;
}
