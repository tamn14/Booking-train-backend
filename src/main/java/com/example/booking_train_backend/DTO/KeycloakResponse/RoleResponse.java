package com.example.booking_train_backend.DTO.KeycloakResponse;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class) // mapping lai theo Json ma keycloak tra ve
public class RoleResponse {
     String id;
     String name;
     String description;
     boolean composite;
}
