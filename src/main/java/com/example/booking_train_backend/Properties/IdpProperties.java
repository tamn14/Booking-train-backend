package com.example.booking_train_backend.Properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
//  ---------------- CLASS NAY DE QUAN LY TAP TRUNG CAC CAU HINH IDP ----------------//
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "idp")
public class IdpProperties {
    private String url;
    private String clientId;
    private String clientSecret;
    private String realm;
}
