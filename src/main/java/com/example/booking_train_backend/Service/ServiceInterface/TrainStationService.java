package com.example.booking_train_backend.Service.ServiceInterface;

import com.example.booking_train_backend.DTO.Request.TrainStationRequest;
import com.example.booking_train_backend.DTO.Response.TrainStationResponse;

import java.util.List;

public interface TrainStationService {
    public TrainStationResponse addTrainStation(TrainStationRequest request) ;
    public TrainStationResponse findByStationName(String name) ;
    public List<TrainStationResponse> findAll() ;

}
