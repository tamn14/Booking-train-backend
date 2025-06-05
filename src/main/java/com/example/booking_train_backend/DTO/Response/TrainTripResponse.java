package com.example.booking_train_backend.DTO.Response;

import lombok.*;

import java.sql.Time;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainTripResponse {
    private Date departureDate ;
    private Time departureTime ;
}
