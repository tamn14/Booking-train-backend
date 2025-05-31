package com.example.booking_train_backend.Repo;

import com.example.booking_train_backend.Entity.Booking;
import com.example.booking_train_backend.Entity.JourneyCarriage;
import com.example.booking_train_backend.Entity.JourneyCarriageId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JourneyCarriageRepo extends JpaRepository<JourneyCarriage, JourneyCarriageId> {
}
