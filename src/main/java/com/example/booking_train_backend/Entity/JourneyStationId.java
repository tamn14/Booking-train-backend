package com.example.booking_train_backend.Entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class JourneyStationId {
    private int journeyId;
    private int stationId;

}
