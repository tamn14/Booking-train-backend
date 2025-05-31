package com.example.booking_train_backend.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id  ;

    @ManyToOne(
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.PERSIST ,
                    CascadeType.DETACH ,
                    CascadeType.REFRESH
            }

    )
    @JoinColumn(name = "passengerId")
    private Passenger passenger ;

    @ManyToOne (
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.PERSIST ,
                    CascadeType.DETACH ,
                    CascadeType.REFRESH
            }

    )
    @JoinColumn(name = "bookingStatusId")
    private BookingStatus bookingStatus ;

    @ManyToOne (
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.PERSIST ,
                    CascadeType.DETACH ,
                    CascadeType.REFRESH
            }

    )
    @JoinColumn(name = "trainStationStart")
    private TrainStation trainStationStart ;

    @ManyToOne (
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.PERSIST ,
                    CascadeType.DETACH ,
                    CascadeType.REFRESH
            }

    )
    @JoinColumn(name = "trainStationEnd")
    private TrainStation trainStationEnd ;

    @ManyToOne (
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.PERSIST ,
                    CascadeType.DETACH ,
                    CascadeType.REFRESH
            }

    )
    @JoinColumn(name = "trainJourneyId")
    private TrainJourney trainJourney;

    @ManyToOne (
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.PERSIST ,
                    CascadeType.DETACH ,
                    CascadeType.REFRESH
            }

    )
    @JoinColumn(name = "carriageClassId")
    private CarriageClass carriageClass ;


    private LocalDateTime bookingDate ;
    private int amountPaid  ;
    private int ticketNo ;
    private int seatNo;
}

