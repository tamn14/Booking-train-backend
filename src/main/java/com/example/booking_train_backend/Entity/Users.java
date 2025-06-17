package com.example.booking_train_backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "passenger" , uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private int id  ;
    private String userKeycloakId;
    private String userName;
    private String lastName  ;
    private String firstName ;
    @Column(nullable = false , unique = true)
    private String email ;


    @OneToMany(
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.MERGE ,
                    CascadeType.PERSIST  ,
                    CascadeType.REFRESH
            } ,
            mappedBy =  "users"

    )
    private List<Booking> bookings = new ArrayList<>() ;
    private LocalDateTime deletedAt;
}
