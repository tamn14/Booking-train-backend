package com.example.booking_train_backend.Config;

// ------------- file nay dung de tao ngay mot tai khoan admin khi vua khoi dong --------------------//

import com.example.booking_train_backend.DTO.KeyloakRequest.Credential;
import com.example.booking_train_backend.DTO.KeyloakRequest.UserCreationParam;
import com.example.booking_train_backend.DTO.Request.UsersRequest;
import com.example.booking_train_backend.Entity.Users;
import com.example.booking_train_backend.Properties.IdpProperties;
import com.example.booking_train_backend.Repo.IdentityProviderRepo;
import com.example.booking_train_backend.Repo.UsersRepo;
import com.example.booking_train_backend.Service.ServiceInterface.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@Slf4j
public class ApplicationConfig {




}
