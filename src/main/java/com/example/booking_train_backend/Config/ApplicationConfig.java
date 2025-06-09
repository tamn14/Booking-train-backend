package com.example.booking_train_backend.Config;

// ------------- file nay dung de tao ngay mot tai khoan admin khi vua khoi dong --------------------//

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@Slf4j
public class ApplicationConfig {
    PasswordEncoder passwordEncoder ;


}
