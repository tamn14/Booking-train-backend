package com.example.booking_train_backend.Repo;

import com.example.booking_train_backend.Entity.TrainTrip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainTripRepo extends JpaRepository<TrainTrip , Integer> {
}
