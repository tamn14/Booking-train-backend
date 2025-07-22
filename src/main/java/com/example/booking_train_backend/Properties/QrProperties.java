package com.example.booking_train_backend.Properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "payment")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QrProperties {
    String vnp_TmnCode ;
    String vnp_HashSecret ;
    String vnp_Url ;
    String returnUrl ;
}