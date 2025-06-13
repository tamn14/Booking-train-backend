package com.example.booking_train_backend.Service.ServiceInterfaceImplement;

import com.example.booking_train_backend.DTO.KeyloakRequest.Credential;
import com.example.booking_train_backend.DTO.KeyloakRequest.UserCreationParam;
import com.example.booking_train_backend.DTO.Request.ChangePasswordRequest;
import com.example.booking_train_backend.DTO.Request.LoginRequest;
import com.example.booking_train_backend.DTO.Request.UsersRequest;
import com.example.booking_train_backend.DTO.Request.UsersUpdateRequest;
import com.example.booking_train_backend.DTO.Response.LoginResponse;
import com.example.booking_train_backend.DTO.Response.UsersResponse;
import com.example.booking_train_backend.Entity.Users;
import com.example.booking_train_backend.Properties.IdpProperties;
import com.example.booking_train_backend.Repo.IdentityProviderRepo;
import com.example.booking_train_backend.Repo.UsersRepo;
import com.example.booking_train_backend.Service.ServiceInterface.KeycloakClientTokenService;
import com.example.booking_train_backend.Service.ServiceInterface.AuthenticationService;
import com.example.booking_train_backend.Service.ServiceInterface.UserService;
import com.example.booking_train_backend.exception.AppException;
import com.example.booking_train_backend.exception.ErrorCode;
import com.example.booking_train_backend.exception.KeycloakNormalizer;
import com.example.booking_train_backend.mapper.PassengerMapper;
import feign.FeignException;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class UserServiceImplement implements UserService {
    private UsersRepo usersRepo ;
    private IdentityProviderRepo identityProviderRepo ;
    private PassengerMapper passengerMapper ;
    private KeycloakClientTokenService keycloakClientTokenService ;
    private AuthenticationService keycloakUserTokenService ;
    private IdpProperties idpProperties ;
    private KeycloakNormalizer keycloakNormalizer ;





    @Override
    public String extractUserId(ResponseEntity<?> responseEntity) {
        // lay ra Location
        // Response ma keycloak tra ve gom HTTP va Location vd nhu sau :
        // HTTP/1.1 201 Created
        //Location: http://localhost:8080/admin/realms/myrealm/users/1a2b3c4d-5678-90ab-cdef-1234567890ab
        String Location = Objects.requireNonNull(responseEntity.getHeaders().get("Location").getFirst()) ;
        // tach theo dau '/'
        String[] strings = Location.split("/") ;
        return strings[strings.length-1] ;
    }


    @Override
    public UsersResponse createUser(UsersRequest request) {
        Users passenger = usersRepo.findByUserName(request.getUserName()) ;
        if((passenger != null)) {
            throw new AppException(ErrorCode.USER_EXISTED) ;

        }
        if (usersRepo.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        try {
            // lay access Token de co the goi API keycloak tao nguoi dung
            var accessToken = keycloakClientTokenService.getAccessToken() ;
            var creationResponse = identityProviderRepo.createUser(
                    idpProperties.getRealm() ,
                    "Bearer " + accessToken ,
                    UserCreationParam.builder()
                            .username(request.getUserName())
                            .email(request.getEmail())
                            .lastName(request.getLastName())
                            .firstName(request.getFirstName())
                            .enabled(true)
                            .emailVerified(false)
                            .credentials(List.of(Credential.builder()
                                    .type("password")
                                    .value(request.getPassword())
                                    .temporary(false)
                                    .build()))
                            .build());

            // luu thong tin vao db
            String userKeycloakId = extractUserId(creationResponse) ;
            Users users = passengerMapper.toEntity(request) ;
            users.setUserName(request.getUserName());
            users.setUserKeycloakId(userKeycloakId);
            users.setFirstName(request.getFirstName());
            users.setEmail(request.getEmail());
            // luu user vao db ( vi day la he thong booking nen co the hieu khi tao user moi la tao passenger moi)
            usersRepo.save(users) ;
            return passengerMapper.toDTO(users) ;


        }
        catch (FeignException feignException){
            throw keycloakNormalizer.handleKeycloakException(feignException);
        }

    }

    @Override
    @PreAuthorize( "hasRole('ADMIN')")
    public List<UsersResponse> getAllUsers() {
        return usersRepo.findAll().stream().map(passengerMapper::toDTO).toList();

    }

    @Override
    @PreAuthorize( "hasRole('ADMIN')")
    public UsersResponse getUserById(int id) {
        Users passenger = usersRepo.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED)) ;
        return passengerMapper.toDTO(passenger) ;

    }

    @Override
    @PostAuthorize("returnObject.userName == authentication.name")
    public UsersResponse updateUser(UsersUpdateRequest usersRequest, int id) {
        Users passenger = usersRepo.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED)) ;

        // kiem tra co phai cap nhat cho user dang dang nhap khong
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();
        if (!passenger.getUserName().equals(currentUsername)) {
            throw new AccessDeniedException("You are not allowed to update this user.");
        }
        // cap nhat thong tin users
        try {
            // lay accessToken cua user
            // do user chi duoc cap nhat thong tin cua chinh user dang dang nhap nen khong dung accessToken cua client ma phai token cua user
            Jwt jwt = (Jwt) auth.getPrincipal();
            String accessToken = jwt.getTokenValue();
            // cap nhat thong tin tren keycloak
            identityProviderRepo.updateUser(
                    idpProperties.getRealm(),
                    "Bearer " + accessToken,
                    passenger.getUserKeycloakId() ,
                    usersRequest
            );
            // cap nhat trong db
            passenger.setLastName(usersRequest.getLastName());
            passenger.setFirstName(usersRequest.getFirstName());
            passenger.setUserName(usersRequest.getUserName());
            // kiem tra email
            if (usersRequest.getEmail() != null && !usersRequest.getEmail().equals(passenger.getEmail())) {
                if (usersRepo.existsByEmail(usersRequest.getEmail())) {
                    throw new IllegalArgumentException("Email already exists");
                }
                passenger.setEmail(usersRequest.getEmail());
            }
            usersRepo.save(passenger) ;

            return passengerMapper.toDTO(passenger) ;

        } catch (FeignException feignException){
            throw keycloakNormalizer.handleKeycloakException(feignException);
        }

    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public UsersResponse getMyInfor() {
        var authentication = SecurityContextHolder.getContext().getAuthentication() ;
        // lay userId cua user dang dang nhap
        String userKeycloakId = authentication.getName() ;
        var user = usersRepo.findByUserKeycloakId(userKeycloakId) ;
        return passengerMapper.toDTO(user) ;

    }



    @Override
    public void changePassword(int userId, ChangePasswordRequest request) {

        Users passenger = usersRepo.findById(userId)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED)) ;

        // kiem tra co phai cap nhat cho user dang dang nhap khong
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();
        if (!passenger.getUserName().equals(currentUsername)) {
            throw new AccessDeniedException("You are not allowed to update this user.");
        }

        // kiem tra co mat khau cu co khop khong
        //Y tuong la lay accessToken cua user neu duoc la dung
        try {
            keycloakUserTokenService.getAccessToken(
                    new LoginRequest(currentUsername, request.getOldPassword())
            );
        } catch (FeignException.Unauthorized e) {
            throw new AccessDeniedException("Old password is incorrect");
        }

        try {
            // lay accessToken cua user
            // do user chi duoc cap nhat thong tin cua chinh user dang dang nhap nen khong dung accessToken cua client ma phai token cua user
            Jwt jwt = (Jwt) auth.getPrincipal();
            String accessToken = jwt.getTokenValue();
            // cap nhat mat khau tren keycloak
            identityProviderRepo.resetUserPassword(
                    idpProperties.getRealm() ,
                    "Bearer " + accessToken,
                    passenger.getUserKeycloakId() ,
                    Credential.builder()
                            .type("password")
                            .value(request.getNewPassword())
                            .temporary(false)
                            .build()
            );

        }
        catch (FeignException feignException){
            throw keycloakNormalizer.handleKeycloakException(feignException);
        }
    }

    @Override
    @PreAuthorize( "hasRole('ADMIN')")
    public void deleteUser(int id) {
        Users passenger = usersRepo.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED)) ;
        usersRepo.deleteById(passenger.getId());

    }
}
