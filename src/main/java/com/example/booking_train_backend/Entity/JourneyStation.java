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

    @MapsId("journeyId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainStationId", nullable = false)
    private TrainStation trainStation ;

    @MapsId("stationId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainJourneyId", nullable = false)
    private TrainJourney trainJourney ;

}
