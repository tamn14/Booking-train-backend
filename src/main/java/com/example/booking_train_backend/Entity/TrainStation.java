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
        @UniqueConstraint(columnNames = "stationName")
})

public class TrainStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;
    @Column(nullable = false , unique = true)
    private String stationName ;

    @OneToMany(
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.MERGE ,
                    CascadeType.PERSIST ,
                    CascadeType.REFRESH
            } ,
            mappedBy = "trainStationStart"

    )
    private List<Booking> trainStationStart = new ArrayList<>() ;



    @OneToMany(
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.MERGE ,
                    CascadeType.PERSIST ,
                    CascadeType.REFRESH
            } ,
            mappedBy = "trainStationEnd"

    )
    private List<Booking> trainStationEnd = new ArrayList<>() ;

    @OneToMany(
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.MERGE ,
                    CascadeType.PERSIST ,
                    CascadeType.REFRESH
            } ,
            mappedBy = "trainStation"

    )
    private List<JourneyStation> journeyStations = new ArrayList<>() ;

}
