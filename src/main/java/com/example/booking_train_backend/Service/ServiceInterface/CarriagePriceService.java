package com.example.booking_train_backend.Service.ServiceInterface;

import com.example.booking_train_backend.DTO.Request.CarriagePriceRequest;
import com.example.booking_train_backend.DTO.Request.CarriagePriceUpdateRequest;
import com.example.booking_train_backend.DTO.Response.CarriagePriceResponse;
import com.example.booking_train_backend.Entity.CarriagePriceId;

import java.util.List;

public interface CarriagePriceService {
    public CarriagePriceResponse addCarriagePrice(CarriagePriceRequest request) ;
    public CarriagePriceResponse updateCarriagePrice (CarriagePriceUpdateRequest request ,int scheduleId , int carriageClassId ) ;

}
