package com.example.booking_train_backend.Util;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;
@Component
public class Extract {
    public String extractUserId(ResponseEntity<?> responseEntity) {
        // lay ra Location
        // Response ma keycloak tra ve gom HTTP va Location vd nhu sau :
        // HTTP/1.1 201 Created
        //Location: http://localhost:8080/admin/realms/myrealm/users/1a2b3c4d-5678-90ab-cdef-1234567890ab
        String Location = Objects.requireNonNull(responseEntity.getHeaders().get("Location")).get(0);
        // tach theo dau '/'
        String[] strings = Location.split("/") ;
        return strings[strings.length-1] ;
    }
}
