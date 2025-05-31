package com.example.booking_train_backend.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})

public class CarriageClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true, nullable = false )
    private String name ;
    private int seatingCapacity ;

    @OneToMany(
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.MERGE ,
                    CascadeType.PERSIST  ,
                    CascadeType.REFRESH
            } ,
            mappedBy =  "carriageClass"

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
            mappedBy =  "carriageClass"

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
            mappedBy =  "carriageClass"

    )
    private List<CarriagePrice> carriagePrices = new ArrayList<>() ;

}
