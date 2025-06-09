package com.example.booking_train_backend.DTO.KeycloakResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientTokenExchangeResponse {
    String grant_type;
    String client_id;
    String client_secret;
    String scope;
}
