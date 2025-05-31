package com.example.booking_train_backend.DTO.Response;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JourneyStationResponse {
    private String trainJourney ;
    private String trainStation ;
    private int stopOrder ;
    private LocalDateTime departureTime ;

}
