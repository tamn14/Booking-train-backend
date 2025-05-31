package com.example.booking_train_backend.DTO.Response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingResponse {
    private PassengerResponse passenger ;
    private String bookingStatus ;
    private String trainStationStart ;
    private String trainStationEnd ;
    private int trainJourney  ;
    private String carriageClass ;
}
