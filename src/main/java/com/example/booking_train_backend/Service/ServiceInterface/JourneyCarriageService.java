package com.example.booking_train_backend.Service.ServiceInterface;

import com.example.booking_train_backend.DTO.Request.JourneyCarriageRequest;
import com.example.booking_train_backend.DTO.Request.JourneyCarriageUpdateRequest;
import com.example.booking_train_backend.DTO.Request.JourneyStationRequest;
import com.example.booking_train_backend.DTO.Request.JourneyStationUpdateRequest;
import com.example.booking_train_backend.DTO.Response.TrainJourneyResponse;
import com.example.booking_train_backend.Entity.JourneyCarriageId;
import com.example.booking_train_backend.Entity.JourneyStationId;

public interface JourneyCarriageService {
    // Them JourneyCarriage moi cho TrainJourney , TrainJourneyId dung de kiem tra chac chan TrainJourney can them da ton tai
    public TrainJourneyResponse addJourneyCarriage (JourneyCarriageRequest request, int TrainJourneyId) ;
    // Cap nhat JourneyCarriage cho TrainJourney
    // vi trong TrainJourney co nhieu JourneyCarriage nen can dam bao cap nhat dung TrainJourneyId va JourneyCarriageId nao
    public TrainJourneyResponse updateJourneyCarriage (JourneyCarriageUpdateRequest request , int TrainJourneyId ) ;
    // Xoa JourneyCarriageId cua Trainjourney nao
    public void deleteJourneyCarriage(int TrainJourneyId , int carriageClassId) ;
}
