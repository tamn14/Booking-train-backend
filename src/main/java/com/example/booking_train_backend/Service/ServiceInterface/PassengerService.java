package com.example.booking_train_backend.Service.ServiceInterface;

import com.example.booking_train_backend.DTO.Response.PassengerResponse;

import java.util.List;

public interface PassengerService {
    public List<PassengerResponse> findAll() ;
    public PassengerResponse findById(int id) ;

}
