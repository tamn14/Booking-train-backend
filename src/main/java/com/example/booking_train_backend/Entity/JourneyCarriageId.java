package com.example.booking_train_backend.Entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class JourneyCarriageId {
    private int journeyId;
    private int carriageClassId;

}
