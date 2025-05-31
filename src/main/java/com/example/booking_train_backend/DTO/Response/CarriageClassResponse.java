package com.example.booking_train_backend.DTO.Response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarriageClassResponse {
    private String className ;
    private int seatingCapacity ;

}
