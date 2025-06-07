package com.example.booking_train_backend.Repo;

import com.example.booking_train_backend.Entity.Booking;
import com.example.booking_train_backend.Entity.TrainJourney;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainJourneyRepo extends JpaRepository<TrainJourney, Integer> {
    public List<TrainJourney> findByName(String name) ;
}
