package com.example.booking_train_backend.DTO.Request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarriagePriceRequest {
    private int scheduleId ;
    private int carriageClassId ;
    private int price ;


}
