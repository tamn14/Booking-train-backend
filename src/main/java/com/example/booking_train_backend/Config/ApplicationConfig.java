package com.example.booking_train_backend.Config;

// ------------- file nay dung de tao ngay mot tai khoan admin khi vua khoi dong --------------------//

import com.example.booking_train_backend.DTO.KeycloakRequest.Credential;
import com.example.booking_train_backend.DTO.KeycloakRequest.UserCreationParam;
import com.example.booking_train_backend.Entity.Users;
import com.example.booking_train_backend.Properties.IdpProperties;
import com.example.booking_train_backend.Properties.RoleTemplate;
import com.example.booking_train_backend.Repo.IdentityProviderRepo;
import com.example.booking_train_backend.Repo.UsersRepo;
import com.example.booking_train_backend.Service.ServiceInterface.KeycloakClientTokenService;
import com.example.booking_train_backend.Util.Extract;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationConfig {
    @Bean
    ApplicationRunner applicationRunner(UsersRepo usersRepo,
                                        IdentityProviderRepo identityProviderRepo,
                                        KeycloakClientTokenService keycloakClientTokenService,
                                        IdpProperties idpProperties,
                                        Extract extract) {
        return args -> {
            if (!usersRepo.existsByUserName("admin")) {

                // lay access Token de co the goi API keycloak tao nguoi dung
                var accessToken = keycloakClientTokenService.getAccessToken();
                var creationResponse = identityProviderRepo.createUser(
                        idpProperties.getRealm(),
                        "Bearer " + accessToken,
                        UserCreationParam.builder()
                                .username("admin")
                                .email("Admin@gmail.com")
                                .lastName("admin")
                                .firstName("admin")
                                .enabled(true)
                                .emailVerified(true)
                                .credentials(List.of(Credential.builder()
                                        .type("password")
                                        .value("admin")
                                        .temporary(false)
                                        .build()))
                                .build());
                String userKeycloakId = extract.extractUserId(creationResponse);

                // gan role cho user vua tao
                var role = identityProviderRepo.getRealmRoleByName(idpProperties.getRealm(),
                        "Bearer " + accessToken,
                        RoleTemplate.ADMIN.getValue()
                );
                identityProviderRepo.assignRealmRolesToUser(
                        idpProperties.getRealm(),
                        "Bearer " + accessToken,
                        userKeycloakId,
                        List.of(role)
                );

                // luu thong tin vao db
                Users users = Users.builder()
                        .userName("admin")
                        .lastName("admin")
                        .firstName("admin")
                        .email("Admin@gmail.com")
                        .userKeycloakId(userKeycloakId)
                        .build();
                // luu user vao db ( vi day la he thong booking nen co the hieu khi tao user moi la tao passenger moi)
                usersRepo.save(users);
                
            }

        };
    }
}