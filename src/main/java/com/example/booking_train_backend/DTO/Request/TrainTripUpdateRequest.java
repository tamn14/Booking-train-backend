package com.example.booking_train_backend.DTO.Request;

import lombok.*;

import java.sql.Time;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainTripUpdateRequest {
    private int id ;
    private Date departureDate ;
    private Time departureTime ;
}
