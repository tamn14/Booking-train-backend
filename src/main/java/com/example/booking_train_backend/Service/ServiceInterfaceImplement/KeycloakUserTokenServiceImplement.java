package com.example.booking_train_backend.Service.ServiceInterfaceImplement;

import com.example.booking_train_backend.DTO.KeycloakResponse.UserTokenExchangeResponse;
import com.example.booking_train_backend.DTO.KeyloakRequest.UserAccessTokenExchangeParam;
import com.example.booking_train_backend.DTO.KeyloakRequest.UserRefreshTokenExchangeParam;
import com.example.booking_train_backend.DTO.Request.LoginRequest;
import com.example.booking_train_backend.DTO.Request.LogoutRequest;
import com.example.booking_train_backend.DTO.Request.RefreshRequest;
import com.example.booking_train_backend.DTO.Response.AuthenticationResponse;
import com.example.booking_train_backend.Model.Auth.TokenInfo;
import com.example.booking_train_backend.Properties.IdpProperties;
import com.example.booking_train_backend.Repo.IdentityProviderRepo;
import com.example.booking_train_backend.Repo.UsersRepo;
import com.example.booking_train_backend.Service.ServiceInterface.AuthenticationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional
public class KeycloakUserTokenServiceImplement implements AuthenticationService {
    private UsersRepo usersRepo ;;
    private IdentityProviderRepo identityProviderRepo ;
    private IdpProperties idpProperties ;
    @Autowired
    public KeycloakUserTokenServiceImplement(UsersRepo usersRepo,
                                             IdentityProviderRepo identityProviderRepo,
                                             IdpProperties idpProperties) {
        this.usersRepo = usersRepo;
        this.identityProviderRepo = identityProviderRepo;
        this.idpProperties = idpProperties;
    }
    // ten dang nhap va token tuong ung
    private final Map<String, TokenInfo> tokenStore = new ConcurrentHashMap<>();

    private TokenInfo requestNewToken(LoginRequest loginRequest) {
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
        String userName = loginRequest.getUserName();
        TokenInfo tokenInfo = tokenStore.get(userName) ;
        if(tokenInfo == null || tokenInfo.getTokenExpiry() == null || Instant.now().isAfter(tokenInfo.getTokenExpiry().minusSeconds(60))){
            tokenInfo = requestNewToken(loginRequest);
            tokenStore.put(userName , tokenInfo) ;
        }

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
        String userName = loginRequest.getUserName();
        TokenInfo tokenInfo = tokenStore.get(userName) ;
        if(tokenInfo == null || tokenInfo.getTokenExpiry() == null || Instant.now().isAfter(tokenInfo.getTokenExpiry().minusSeconds(60))){
            tokenInfo = requestNewToken(loginRequest);
            tokenStore.put(userName , tokenInfo) ;
        }
        return tokenInfo.getCachedToken() ;
    }



    @Override
    public TokenInfo refreshToken(RefreshRequest refreshRequest) {
        String refreshToken = refreshRequest.getToken();
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new IllegalArgumentException("Refresh token must not be empty");
        }

        UserRefreshTokenExchangeParam param = UserRefreshTokenExchangeParam.builder()
                .grant_type("refresh_token")
                .client_id(idpProperties.getClientId())
                .client_secret(idpProperties.getClientSecret())
                .refresh_token(refreshToken)
                .build();

        UserTokenExchangeResponse response = identityProviderRepo.exchangeUserRefreshToken(idpProperties.getRealm(), param);
        return new TokenInfo(
                response.getAccessToken(),
                Instant.now().plusSeconds(Long.parseLong(response.getExpiresIn())),
                response.getRefreshToken(),
                Instant.now().plusSeconds(Long.parseLong(response.getRefreshExpiresIn()))
        );
    }

    @Override
    public void logout(LogoutRequest logoutRequest) {
        String token = logoutRequest.getToken() ;
        if(token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Refresh token must not be empty");
        }
        // revoke token
        identityProviderRepo.revokeUserToken(
                idpProperties.getRealm(),
                idpProperties.getClientId(),
                idpProperties.getClientSecret(),
                token
        );
        // xoa token khoi cache
        tokenStore.entrySet().removeIf(entry -> entry.getValue().getCachedToken().equals(token));
    }
}
