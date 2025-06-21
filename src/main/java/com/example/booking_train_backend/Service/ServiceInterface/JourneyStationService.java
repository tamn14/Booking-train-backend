package com.example.booking_train_backend.Service.ServiceInterface;

import com.example.booking_train_backend.DTO.Request.JourneyStationRequest;
import com.example.booking_train_backend.DTO.Request.JourneyStationUpdateRequest;
import com.example.booking_train_backend.DTO.Request.TrainJourneyRequest;
import com.example.booking_train_backend.DTO.Request.TrainTripRequest;
import com.example.booking_train_backend.DTO.Response.TrainJourneyResponse;
import com.example.booking_train_backend.Entity.JourneyStationId;

public interface JourneyStationService {
    // Them JourneyStation moi cho TrainJourney , TrainJourneyId dung de kiem tra chac chan TrainJourney can them da ton tai
    public TrainJourneyResponse addJourneyStation(JourneyStationRequest request  , int TrainJourneyId) ;
    // Cap nhat JourneyStation cho TrainJourney
    // vi trong TrainJourney co nhieu JourneyStation nen can dam bao cap nhat dung TrainJourneyId va JourneyStationId nao
    public TrainJourneyResponse updateJourneyStation(JourneyStationUpdateRequest request , int TrainJourneyId ) ;
    // Xoa JourneyStationId cua Trainjourney nao
    public void deleteJourneyStation(int TrainJourneyId ,int TrainStation) ;
}
