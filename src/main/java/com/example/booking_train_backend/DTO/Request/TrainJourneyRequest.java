package com.example.booking_train_backend.DTO.Request;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainJourneyRequest {
    private String name ;
    private List<JourneyStationRequest> journeyStationRequests = new ArrayList<>() ;
    private List<JourneyCarriageRequest> journeyCarriageRequests = new ArrayList<>() ;
    private int schedule ;
    private List<TrainTripRequest> trainTripRequest ;


}
