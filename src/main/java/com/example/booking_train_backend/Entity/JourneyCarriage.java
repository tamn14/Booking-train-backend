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

    @MapsId("trainJourneyId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_journey_Id")
    private TrainJourney trainJourney ;

    @MapsId("carriageClassId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carriage_class_Id")
    private CarriageClass carriageClass ;

    private int position ;

}
