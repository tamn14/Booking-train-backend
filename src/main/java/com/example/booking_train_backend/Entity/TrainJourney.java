package com.example.booking_train_backend.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
public class TrainJourney {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;
    @Column(unique = true , nullable = false)
    private String name ;
    @OneToMany(
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.MERGE ,
                    CascadeType.PERSIST  ,
                    CascadeType.REFRESH
            } ,
            mappedBy =  "trainJourney"

    )
    private List<Booking> bookings = new ArrayList<>() ;

    @OneToMany(
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.MERGE ,
                    CascadeType.PERSIST  ,
                    CascadeType.REFRESH
            } ,
            mappedBy =  "trainJourney"

    )
    private List<JourneyCarriage> journeyCarriages = new ArrayList<>() ;

    @OneToMany(
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.MERGE ,
                    CascadeType.PERSIST  ,
                    CascadeType.REFRESH
            } ,
            mappedBy =  "trainJourney"

    )
    private List<JourneyStation> journeyStations = new ArrayList<>() ;

    @ManyToOne (
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.PERSIST ,
                    CascadeType.MERGE ,
                    CascadeType.REFRESH
            }

    )
    @JoinColumn(name = "scheduleId")
    private Schedule schedule ;

    @OneToMany(
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.MERGE ,
                    CascadeType.PERSIST  ,
                    CascadeType.REFRESH
            } ,
            mappedBy =  "trainJourney"

    )
    private List<TrainTrip> trainTrips = new ArrayList<>() ;


}
