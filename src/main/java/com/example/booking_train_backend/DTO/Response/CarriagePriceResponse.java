package com.example.booking_train_backend.DTO.Response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarriagePriceResponse {
    private ScheduleResponse schedule ;
    private CarriageClassResponse carriageClass ;
    private int price ;

}
