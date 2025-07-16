package com.example.booking_train_backend.Entity;

import com.example.booking_train_backend.Properties.StatusBooking;
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
public class BookingStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id  ;
    @Enumerated(EnumType.STRING)
    private StatusBooking name ;
    @OneToMany(
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.MERGE ,
                    CascadeType.PERSIST ,
                    CascadeType.REFRESH
            } ,
            mappedBy = "bookingStatus"

    )
    private List<Booking> bookings = new ArrayList<>() ;

}
