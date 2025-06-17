package com.example.booking_train_backend.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class CarriagePriceId {
    @Column(name = "schedule_Id")
    private int scheduleId;
    @Column(name = "carriage_class_Id")
    private int carriageClassId;

}
