package com.example.booking_train_backend.DTO.Response;

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
    private List<JourneyStationResponse> journeyStationRespones = new ArrayList<>() ;
    private List<JourneyCarriageResponse> journeyCarriageResponses = new ArrayList<>();
    private String schedule ;


}
