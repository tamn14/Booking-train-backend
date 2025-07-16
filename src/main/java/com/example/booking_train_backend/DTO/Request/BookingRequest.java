package com.example.booking_train_backend.DTO.Request;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingRequest {
    private int passenger ;
//    private int bookingStatus ;
    private String trainStationStart ;
    private String trainStationEnd ;
    private int trainJourney  ;
    private String carriageClass ;
    private LocalDateTime bookingDate ;
    private int amountPaid  ;
    private int ticketNo ;
    private int seatNo;
}
