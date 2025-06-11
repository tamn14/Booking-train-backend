package com.example.booking_train_backend.DTO.Request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersRequest {
    private String userName ;
    private String firstName ;
    private String lastName ;
    private String email ;


}
