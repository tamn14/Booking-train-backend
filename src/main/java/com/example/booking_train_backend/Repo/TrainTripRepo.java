package com.example.booking_train_backend.Repo;

import com.example.booking_train_backend.Entity.TrainJourney;
import com.example.booking_train_backend.Entity.TrainTrip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainTripRepo extends JpaRepository<TrainTrip , Integer> {
    List<TrainTrip> findByTrainJourney(TrainJourney trainJourney);
}
