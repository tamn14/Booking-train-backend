package com.example.booking_train_backend.DTO.Request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JourneyCarriageUpdateRequest {
    private Integer trainJourneyId ;
    private Integer carriageClass ;
    private Integer position ;

}
