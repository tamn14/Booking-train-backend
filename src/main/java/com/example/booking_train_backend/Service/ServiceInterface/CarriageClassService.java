package com.example.booking_train_backend.Service.ServiceInterface;

import com.example.booking_train_backend.DTO.Request.CarriageClassRequest;
import com.example.booking_train_backend.DTO.Response.CarriageClassResponse;
import com.example.booking_train_backend.Entity.CarriageClass;

import java.util.List;

public interface CarriageClassService {
    public CarriageClassResponse add (CarriageClassRequest request) ;
    public CarriageClassResponse updateCarriageClass (CarriageClassRequest request , int id) ;
    public List<CarriageClassResponse> findAll() ;
}
