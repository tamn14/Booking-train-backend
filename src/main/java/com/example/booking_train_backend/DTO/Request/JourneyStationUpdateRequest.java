package com.example.booking_train_backend.DTO.Request;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JourneyStationUpdateRequest {
    private Integer journeyId ;
    private Integer trainStationId ;
    private LocalDateTime departureTime ;



}
