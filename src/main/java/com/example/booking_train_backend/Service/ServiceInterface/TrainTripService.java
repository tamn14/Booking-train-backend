package com.example.booking_train_backend.Service.ServiceInterface;

import com.example.booking_train_backend.DTO.Request.TrainTripRequest;
import com.example.booking_train_backend.DTO.Request.TrainTripUpdateRequest;
import com.example.booking_train_backend.DTO.Response.TrainJourneyResponse;

public interface TrainTripService {
    // Them TrainTrip moi cho TrainJourney , TrainJourneyId dung de kiem tra chac chan TrainJourney can them da ton tai
    public TrainJourneyResponse addTrainTrip(TrainTripRequest request , int TrainJourneyId) ;
    // Cap nhat TrainTrip cho TrainJourney
    // vi trong TrainJourney co nhieu TrainTrip nen can dam bao cap nhat dung TrainJourneyId va TrainTripId nao
    public TrainJourneyResponse updateTrainTrip (TrainTripUpdateRequest trainTripUpdateRequest , int trainTripId , int trainJourneyId  ) ;
    // Xoa TrainTrip cua Trainjourney nao
    public void deleteTrainTrip (int TrainJourneyId ,int TrainTripId) ;
}
