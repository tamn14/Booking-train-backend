package com.example.booking_train_backend.Repo;

import com.example.booking_train_backend.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepo extends JpaRepository<Users, Integer> {
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    boolean existsById(int id);
    Users findByUserKeycloakId (int id) ;
    Users findByEmail (String Email) ;


}
