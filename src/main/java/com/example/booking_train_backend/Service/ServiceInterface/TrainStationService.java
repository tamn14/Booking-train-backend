package com.example.booking_train_backend.Service.ServiceInterface;

import com.example.booking_train_backend.DTO.Request.TrainStationRequest;
import com.example.booking_train_backend.DTO.Response.TrainStationResponse;

import java.util.List;

public interface TrainStationService {
    public TrainStationResponse add (TrainStationRequest request) ;
    public TrainStationResponse update(TrainStationRequest request) ;
    public void delete ( int id) ;
    public List<TrainStationResponse> findAll() ;

}
