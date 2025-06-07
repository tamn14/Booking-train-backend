package com.example.booking_train_backend.DTO.Request;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JourneyStationRequest {
//    private int journeyId ;
    private int trainStationId ;
    private int stopOrder ;
    private LocalDateTime departureTime ;


}
