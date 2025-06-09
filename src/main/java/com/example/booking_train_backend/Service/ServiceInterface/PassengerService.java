package com.example.booking_train_backend.Service.ServiceInterface;

import com.example.booking_train_backend.DTO.Response.UsersResponse;

import java.util.List;

public interface PassengerService {
    public List<UsersResponse> findAll() ;
    public UsersResponse findById(int id) ;

}
