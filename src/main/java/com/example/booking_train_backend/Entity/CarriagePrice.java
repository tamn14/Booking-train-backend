package com.example.booking_train_backend.Entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class CarriagePrice {
    @EmbeddedId
    private CarriagePriceId id ;
    private int price ;

    @MapsId("scheduleId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheduleId", nullable = false)
    private Schedule schedule ;

    @MapsId("carriageClassId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carriageClassId", nullable = false)
    private CarriageClass carriageClass ;
}
