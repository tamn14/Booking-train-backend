package com.example.booking_train_backend.DTO.Request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingRequest {
    private int passenger ;
    private int bookingStatus ;
    private String trainStationStart ;
    private String trainStationEnd ;
    private int trainJourney  ;
    private String carriageClass ;
}
