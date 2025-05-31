package com.example.booking_train_backend.DTO.Request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarriageClassRequest {
    private String className ;
    private int seatingCapacity ;
}
