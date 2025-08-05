package com.example.booking_train_backend.DTO.KeyloakRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAccessTokenExchangeParam {
    String grant_type;
    String client_id;
    String client_secret;
    String username;
    String password;
    String scope;
}
