package com.example.booking_train_backend.Service.ServiceInterface;

import com.example.booking_train_backend.DTO.Request.TrainJourneyRequest;
import com.example.booking_train_backend.DTO.Request.TrainJourneyUpdateRequest;
import com.example.booking_train_backend.DTO.Response.TrainJourneyResponse;
import com.example.booking_train_backend.DTO.Response.TrainTripResponse;

import java.util.List;

public interface TrainJourneyService {
    public TrainJourneyResponse addTrainJourney (TrainJourneyRequest request) ;
    public TrainJourneyResponse updateTrainJourney (TrainJourneyUpdateRequest request, int id )  ;
    public List<TrainJourneyResponse> findByName(String name) ;
    public TrainJourneyResponse findById(int id) ;

}
