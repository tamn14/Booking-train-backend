package com.example.booking_train_backend.Service.ServiceInterfaceImplement;

import com.example.booking_train_backend.DTO.KeycloakResponse.UserTokenExchangeResponse;
import com.example.booking_train_backend.DTO.KeycloakRequest.GoogleTokenExchangeParam;
import com.example.booking_train_backend.DTO.KeycloakRequest.RevokeUserParam;
import com.example.booking_train_backend.DTO.KeycloakRequest.UserAccessTokenExchangeParam;
import com.example.booking_train_backend.DTO.KeycloakRequest.UserRefreshTokenExchangeParam;
import com.example.booking_train_backend.DTO.Request.LoginRequest;
import com.example.booking_train_backend.DTO.Request.LogoutRequest;
import com.example.booking_train_backend.DTO.Request.RefreshRequest;
import com.example.booking_train_backend.DTO.Response.AuthenticationResponse;
import com.example.booking_train_backend.Model.Auth.TokenInfo;
import com.example.booking_train_backend.Properties.IdpProperties;
import com.example.booking_train_backend.Repo.IdentityProviderRepo;
import com.example.booking_train_backend.Repo.UsersRepo;
import com.example.booking_train_backend.Service.ServiceInterface.AuthenticationService;
import com.example.booking_train_backend.Service.ServiceInterface.TokenBlacklistService;
import com.example.booking_train_backend.exception.AppException;
import com.example.booking_train_backend.exception.ErrorCode;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Transactional
public class KeycloakUserTokenServiceImplement implements AuthenticationService {
    private final UsersRepo usersRepo ;
    private final IdentityProviderRepo identityProviderRepo ;
    private final IdpProperties idpProperties ;
    private final TokenBlacklistService tokenBlacklistService ;
    @Autowired
    public KeycloakUserTokenServiceImplement(UsersRepo usersRepo,
                                             IdentityProviderRepo identityProviderRepo,
                                             IdpProperties idpProperties,
                                             TokenBlacklistService tokenBlacklistService) {
        this.usersRepo = usersRepo;
        this.identityProviderRepo = identityProviderRepo;
        this.idpProperties = idpProperties;
        this.tokenBlacklistService = tokenBlacklistService;
    }


    private void validateActiveUser(String username) {
        var user = usersRepo.findByUserName(username);
        if (user == null || user.getDeletedAt() != null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

    }


    private TokenInfo requestNewToken(LoginRequest loginRequest) {
        // kiem tra neu user da bi xoa trong db (xoa mem) thi khong duoc login
        validateActiveUser(loginRequest.getUserName());
        // tao tokenInfo
        UserAccessTokenExchangeParam param = UserAccessTokenExchangeParam.builder()
                .grant_type("password")
                .client_id(idpProperties.getClientId())
                .client_secret(idpProperties.getClientSecret())
                .username(loginRequest.getUserName())
                .password(loginRequest.getPassword())
                .scope("openid")
                .build();

        UserTokenExchangeResponse response = identityProviderRepo.exchangeUserAccessToken(idpProperties.getRealm(), param);
        return new TokenInfo(
                response.getAccessToken(),
                Instant.now().plusSeconds(Long.parseLong(response.getExpiresIn())),
                response.getRefreshToken(),
                Instant.now().plusSeconds(Long.parseLong(response.getRefreshExpiresIn()))
        );
    }

    @Override
    public AuthenticationResponse login(LoginRequest loginRequest) {
        TokenInfo tokenInfo = requestNewToken(loginRequest);
        return AuthenticationResponse.builder()
                .accessToken(tokenInfo.getCachedToken())
                .refreshToken(tokenInfo.getRefreshToken())
                .tokenType("Bearer")
                .expiresIn(tokenInfo.getTokenExpiry().getEpochSecond() - Instant.now().getEpochSecond())
                .authenticated(true)
                .build() ;


    }

    @Override
    public String getAccessToken(LoginRequest loginRequest) {
        return requestNewToken(loginRequest).getCachedToken();
    }



    @Override
    public AuthenticationResponse refreshToken(RefreshRequest refreshRequest) {
        String refreshToken = refreshRequest.getToken();
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        UserRefreshTokenExchangeParam param = UserRefreshTokenExchangeParam.builder()
                .grant_type("refresh_token")
                .client_id(idpProperties.getClientId())
                .client_secret(idpProperties.getClientSecret())
                .refresh_token(refreshToken)
                .build();

        UserTokenExchangeResponse response = identityProviderRepo.exchangeUserRefreshToken(idpProperties.getRealm(), param);
        TokenInfo tokenInfo =  new TokenInfo(
                response.getAccessToken(),
                Instant.now().plusSeconds(Long.parseLong(response.getExpiresIn())),
                response.getRefreshToken(),
                Instant.now().plusSeconds(Long.parseLong(response.getRefreshExpiresIn()))
        );
        return AuthenticationResponse.builder()
                .accessToken(tokenInfo.getCachedToken())
                .refreshToken(tokenInfo.getRefreshToken())
                .tokenType("Bearer")
                .expiresIn(tokenInfo.getTokenExpiry().getEpochSecond() - Instant.now().getEpochSecond())
                .authenticated(true)
                .build();
    }


    @Override
    public void logout(LogoutRequest logoutRequest) {
        String refreshToken = logoutRequest.getRefreshToken();
        String accessToken = logoutRequest.getAccessToken();
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        RevokeUserParam param = RevokeUserParam.builder()
                .client_id(idpProperties.getClientId())
                .client_secret(idpProperties.getClientSecret())
                .refresh_token(refreshToken)
                .build();

        identityProviderRepo.revokeUserToken(
                idpProperties.getRealm(),
                param
        );
        if (accessToken != null && !accessToken.isEmpty()) {
            tokenBlacklistService.addToBlacklist(accessToken);
        }
    }

    @Override
    public AuthenticationResponse googleLogin(String code) {
        if (code == null || code.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        GoogleTokenExchangeParam tokenExchangeParam = GoogleTokenExchangeParam.builder()
                .grant_type("authorization_code")
                .client_id(idpProperties.getClientId())
                .client_secret(idpProperties.getClientSecret())
                .code(code)
                .redirect_uri(idpProperties.getGoogleRedirectUri())
                .scope("openid")
                .build() ;

        UserTokenExchangeResponse response = identityProviderRepo.exchangeGoogleCodeToken(
                idpProperties.getRealm(),
                tokenExchangeParam
        );

        TokenInfo tokenInfo = new TokenInfo(
                response.getAccessToken(),
                Instant.now().plusSeconds(Long.parseLong(response.getExpiresIn())),
                response.getRefreshToken(),
                Instant.now().plusSeconds(Long.parseLong(response.getRefreshExpiresIn()))
        );

        return AuthenticationResponse.builder()
                .accessToken(tokenInfo.getCachedToken())
                .refreshToken(tokenInfo.getRefreshToken())
                .tokenType("Bearer")
                .expiresIn(tokenInfo.getTokenExpiry().getEpochSecond() - Instant.now().getEpochSecond())
                .authenticated(true)
                .build();
    }
}
