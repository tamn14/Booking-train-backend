package com.example.booking_train_backend.DTO.Request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingUpdateRequest {
    private int id ;
    private String trainStationEnd ;
    private int trainJourney  ;
    private String carriageClass ;
}
