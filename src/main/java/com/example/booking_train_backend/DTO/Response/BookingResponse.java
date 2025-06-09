package com.example.booking_train_backend.DTO.Response;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingResponse {
    private UsersResponse passenger ;
    private String bookingStatus ;
    private String trainStationStart ;
    private String trainStationEnd ;
    private int trainJourney  ;
    private String carriageClass ;
    private LocalDateTime bookingDate ;
    private int amountPaid  ;
    private int ticketNo ;
    private int seatNo;
}
