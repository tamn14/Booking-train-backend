package com.example.booking_train_backend.Repo;

import com.example.booking_train_backend.Entity.Booking;
import com.example.booking_train_backend.Entity.TrainStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainStationRepo extends JpaRepository<TrainStation, Integer> {
    public TrainStation findByStationName(String name) ;
}
