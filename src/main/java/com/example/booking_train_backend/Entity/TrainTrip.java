package com.example.booking_train_backend.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.util.Date;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainTrip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id  ;
    private Date departureDate ;
    private Time departureTime ;
    @ManyToOne(
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.PERSIST ,
                    CascadeType.MERGE ,
                    CascadeType.REFRESH
            }

    )
    @JoinColumn(name = "trainJourneyId")
    private TrainJourney trainJourney;

}
