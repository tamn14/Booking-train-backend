package com.example.booking_train_backend.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class JourneyStationId {
    @Column(name = "train_station_Id")
    private int trainJourneyId;
    @Column(name = "train_journey_Id")
    private int trainStationId;

}
