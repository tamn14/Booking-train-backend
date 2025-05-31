package com.example.booking_train_backend.Repo;

import com.example.booking_train_backend.Entity.Booking;
import com.example.booking_train_backend.Entity.CarriagePrice;
import com.example.booking_train_backend.Entity.CarriagePriceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarriagePriceRepo extends JpaRepository<CarriagePrice, CarriagePriceId> {
}
