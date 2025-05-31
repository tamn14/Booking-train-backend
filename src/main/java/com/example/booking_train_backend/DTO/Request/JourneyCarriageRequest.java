package com.example.booking_train_backend.DTO.Request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JourneyCarriageRequest {
    private int trainJourneyId ;
    private int carriageClass ;
    private int position ;

}
