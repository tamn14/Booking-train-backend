package com.example.booking_train_backend.DTO.Response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PassengerResponse {
    private String firstName ;
    private String lastName ;
    private  String phone ;

}
