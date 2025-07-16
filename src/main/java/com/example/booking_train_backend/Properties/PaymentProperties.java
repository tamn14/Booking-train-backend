package com.example.booking_train_backend.Properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "payment")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentProperties {
     String bankCode ;
     String bankAccount ;
}
