package com.example.booking_train_backend.DTO.KeycloakRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationParam {
    String username;
    boolean enabled;
    String email;
    // thuoc tinh keycloak
    boolean emailVerified;
    String firstName;
    String lastName;
    // thong tin chung thuc cua keycloak
    List<Credential> credentials;
}
