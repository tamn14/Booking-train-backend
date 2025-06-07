package com.example.booking_train_backend.DTO.Request;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainJourneyUpdateRequest {
    private String name ;
    private int schedule ;


}
