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

public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;

    @Column(nullable = false , unique = true)
    private String name ;

    @OneToMany(
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.MERGE ,
                    CascadeType.PERSIST  ,
                    CascadeType.REFRESH
            } ,
            mappedBy =  "schedule"

    )
    private List<TrainJourney> trainJourneys = new ArrayList<>() ;

    @OneToMany(
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.MERGE ,
                    CascadeType.PERSIST  ,
                    CascadeType.REFRESH
            } ,
            mappedBy =  "schedule"

    )
    private List<CarriagePrice> carriagePrices = new ArrayList<>() ;

}
