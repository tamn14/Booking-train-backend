package com.example.booking_train_backend.DTO.Request;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyUserRequest {
    @Pattern(regexp = "^[0-9]{5}$")
    private String accountNumber ;
}
