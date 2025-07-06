package com.example.booking_train_backend.Service.ServiceInterfaceImplement;

import com.example.booking_train_backend.DTO.KeyloakRequest.Credential;
import com.example.booking_train_backend.DTO.KeyloakRequest.UserCreationParam;
import com.example.booking_train_backend.DTO.Request.*;
import com.example.booking_train_backend.DTO.Response.UsersResponse;
import com.example.booking_train_backend.Entity.Users;
import com.example.booking_train_backend.Properties.IdpProperties;
import com.example.booking_train_backend.Properties.RoleTemplate;
import com.example.booking_train_backend.Repo.IdentityProviderRepo;
import com.example.booking_train_backend.Repo.UsersRepo;
import com.example.booking_train_backend.Service.ServiceInterface.EmailService;
import com.example.booking_train_backend.Service.ServiceInterface.KeycloakClientTokenService;
import com.example.booking_train_backend.Service.ServiceInterface.AuthenticationService;
import com.example.booking_train_backend.Service.ServiceInterface.UserService;
import com.example.booking_train_backend.Util.Extract;
import com.example.booking_train_backend.exception.AppException;
import com.example.booking_train_backend.exception.ErrorCode;
import com.example.booking_train_backend.exception.KeycloakNormalizer;
import com.example.booking_train_backend.mapper.PassengerMapper;
import feign.FeignException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.Transactional;
import org.eclipse.angus.mail.imap.protocol.MailboxInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@Transactional
public class UserServiceImplement implements UserService {
    private final UsersRepo  usersRepo ;
    private final IdentityProviderRepo identityProviderRepo ;
    private final PassengerMapper passengerMapper ;
    private final KeycloakClientTokenService keycloakClientTokenService ;
    private final AuthenticationService keycloakUserTokenService ;
    private final IdpProperties idpProperties ;
    private final KeycloakNormalizer keycloakNormalizer ;
    private final Extract extract;
    private final EmailService emailService ;
    @Autowired
    public UserServiceImplement(UsersRepo usersRepo,
                                IdentityProviderRepo identityProviderRepo,
                                PassengerMapper passengerMapper,
                                KeycloakClientTokenService keycloakClientTokenService,
                                AuthenticationService keycloakUserTokenService,
                                IdpProperties idpProperties,
                                KeycloakNormalizer keycloakNormalizer,
                                Extract extract ,
                                EmailService emailService) {
        this.usersRepo = usersRepo;
        this.identityProviderRepo = identityProviderRepo;
        this.passengerMapper = passengerMapper;
        this.keycloakClientTokenService = keycloakClientTokenService;
        this.keycloakUserTokenService = keycloakUserTokenService;
        this.idpProperties = idpProperties;
        this.keycloakNormalizer = keycloakNormalizer;
        this.extract = extract;
        this.emailService = emailService ;
    }

    @Value("${spring.mail.from}")
    private String mailForm ;

    private void checkUserNameExisted(String username) {
        if (usersRepo.findByUserName(username) != null) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
    }

    private String createAccountNumber(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; // hoặc thêm chữ thường nếu cần
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        return sb.toString();
    }

    private void checkEmailExisted (String email) {
        if (usersRepo.existsByEmail(email)) {
            throw new AppException(ErrorCode.EMAIL_EXISTED) ;
        }
    }

    private void checkDeleteAt(Users users) {
        if (users.getDeletedAt() != null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
    }

    private void setValueUpdate(Users passenger , UsersUpdateRequest usersRequest ) {
        passenger.setLastName(usersRequest.getLastName());
        passenger.setFirstName(usersRequest.getFirstName());
        // kiem tra email
        if (usersRequest.getEmail() != null && !usersRequest.getEmail().equals(passenger.getEmail())) {
            if (usersRepo.existsByEmail(usersRequest.getEmail())) {
                throw new AppException(ErrorCode.EMAIL_EXISTED) ;
            }
            passenger.setEmail(usersRequest.getEmail());
        }
    }

    @Override
    public UsersResponse VerifyUsers(VerifyUserRequest verifyUserRequest , int id) {
        Users users = usersRepo.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED)) ;

        if( verifyUserRequest.getAccountNumber().equals(users.getAccountNumber())) {
            users.setEnable(true);
        }
        else {
            users.setEnable(false);
        }
        usersRepo.save(users) ;
        return passengerMapper.toDTO(users);
    }




    @Override
    public UsersResponse createUser(UsersRequest request) {
        // kiem tra user
        checkUserNameExisted(request.getUserName());

        // kiem tra email ton tai
        checkEmailExisted(request.getEmail());

        // tao accountNumber
        String accountNumber = createAccountNumber(5) ;
        String name = request.getLastName()+ request.getFirstName() ;
        // goi accountNumber den email de xac thuc tai khoan
        emailService.verifyEmail(mailForm, request.getEmail(), accountNumber , name);
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
            String userKeycloakId = extract.extractUserId(creationResponse) ;

            // gan role cho user vua tao
            var role = identityProviderRepo.getRealmRoleByName(idpProperties.getRealm(),
                    "Bearer " + accessToken,
                    RoleTemplate.USER.getValue()
            );
            identityProviderRepo.assignRealmRolesToUser(
                    idpProperties.getRealm(),
                    "Bearer " + accessToken,
                    userKeycloakId,
                    List.of(role)
            );

            // luu thong tin vao db
            Users users = passengerMapper.toEntity(request) ;
            users.setEnable(false);
            users.setAccountNumber(accountNumber);
            // luu user vao db ( vi day la he thong booking nen co the hieu khi tao user moi la tao passenger moi)
            usersRepo.save(users) ;
            return passengerMapper.toDTO(users) ;


        } catch (FeignException feignException) {
            throw keycloakNormalizer.handleKeycloakException(feignException);
        }

    }

    @Override
    @PreAuthorize( "hasRole('ADMIN')")
    public List<UsersResponse> getAllUsers() {
        return usersRepo.findByDeletedAtIsNull()
                .stream().map(passengerMapper::toDTO).toList();

    }

    @Override
    @PreAuthorize( "hasRole('ADMIN')")
    public UsersResponse getUserById(int id) {
        Users users = usersRepo.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED)) ;
        return passengerMapper.toDTO(users) ;

    }

    @Override
    @PostAuthorize("returnObject.userName == authentication.name")
    public UsersResponse updateUser(UsersUpdateRequest usersRequest) {
        // lay user hien tai tu phien dang nhap
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();
        // khong can kiem tra user co ton tai khong vi lay tong tin username tu chinh phien dang nhap cua user
        Users passenger = usersRepo.findByUserName(currentUsername) ;



        if (passenger == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        // kiem tra da delete
        checkDeleteAt(passenger);


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
            setValueUpdate(passenger , usersRequest);
            usersRepo.save(passenger) ;

            return passengerMapper.toDTO(passenger) ;

        } catch (FeignException feignException){
            throw keycloakNormalizer.handleKeycloakException(feignException);
        }

    }

    @Override
    @PreAuthorize("hasRole('USER')")
    @PostAuthorize("returnObject.userName == authentication.name")
    public UsersResponse getMyInfor() {
        var authentication = SecurityContextHolder.getContext().getAuthentication() ;
        // lay userId cua user dang dang nhap
        String userKeycloakId = authentication.getName() ;
        var user = usersRepo.findByUserKeycloakId(userKeycloakId) ;

        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        // kiem tra delete
        checkDeleteAt(user);
        return passengerMapper.toDTO(user) ;

    }



    @Override
    @PreAuthorize("hasRole('USER')")
    public void changePassword(ChangePasswordRequest request) {

        // lay user hien tai tu phien dang nhap
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();
        // khong can kiem tra user co ton tai khong vi lay tong tin username tu chinh phien dang nhap cua user
        Users passenger = usersRepo.findByUserName(currentUsername) ;
        if (passenger == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        // kiem tra delete
        checkDeleteAt(passenger);


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
        passenger.setDeletedAt(LocalDateTime.now());
        usersRepo.save(passenger);

    }
}
