package com.example.booking_train_backend.Entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class JourneyCarriage {
    @EmbeddedId
    private JourneyCarriageId id ;

    @MapsId("journeyId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainJourneyId", nullable = false)
    private TrainJourney trainJourney ;

    @MapsId("carriageClassId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carriageClassId", nullable = false)
    private CarriageClass carriageClass ;

    private int position ;

}
