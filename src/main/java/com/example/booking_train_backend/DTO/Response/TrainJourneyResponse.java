package com.example.booking_train_backend.DTO.Response;

import com.example.booking_train_backend.DTO.Request.TrainTripRequest;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainJourneyResponse {
    private String name;
    private List<JourneyStationResponse> journeyStationResponses = new ArrayList<>() ;
    private List<JourneyCarriageResponse> journeyCarriageResponses = new ArrayList<>();
    private String schedule ;
    private List<TrainTripResponse> trainTripResponse ;


}
