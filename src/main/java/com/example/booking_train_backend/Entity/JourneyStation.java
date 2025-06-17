package com.example.booking_train_backend.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class JourneyStation {
    @EmbeddedId
    private JourneyStationId id ;
    private int stopOrder ;
    private LocalDateTime departureTime ;

    @MapsId("trainStationId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_station_Id")
    private TrainStation trainStation ;

    @MapsId("trainJourneyId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_journey_Id")
    private TrainJourney trainJourney ;

}
