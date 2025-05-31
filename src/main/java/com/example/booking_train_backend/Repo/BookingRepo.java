package com.example.booking_train_backend.Repo;

import com.example.booking_train_backend.Entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepo extends JpaRepository<Booking , Integer> {
}
