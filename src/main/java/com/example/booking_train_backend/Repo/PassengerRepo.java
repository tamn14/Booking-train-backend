package com.example.booking_train_backend.Repo;

import com.example.booking_train_backend.Entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepo extends JpaRepository<Passenger, Integer> {
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    boolean existsById(int id);
    Passenger findByUserKeycloakId (int id) ;
    Passenger findByEmail (String Email) ;


}
