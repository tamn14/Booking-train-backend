package com.example.booking_train_backend.Service.ServiceInterfaceImplement;

import com.example.booking_train_backend.DTO.KeycloakResponse.UserTokenExchangeResponse;
import com.example.booking_train_backend.DTO.KeyloakRequest.UserAccessTokenExchangeParam;
import com.example.booking_train_backend.DTO.KeyloakRequest.UserRefreshTokenExchangeParam;
import com.example.booking_train_backend.DTO.Request.LoginRequest;
import com.example.booking_train_backend.Model.Auth.TokenInfo;
import com.example.booking_train_backend.Properties.IdpProperties;
import com.example.booking_train_backend.Repo.IdentityProviderRepo;
import com.example.booking_train_backend.Repo.UsersRepo;
import com.example.booking_train_backend.Service.ServiceInterface.KeycloakUserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class KeycloakUserTokenServiceImplement implements KeycloakUserTokenService {
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

    @Override
    public String getAccessToken(LoginRequest loginRequest) {
        String userName = loginRequest.getUserName();
        TokenInfo tokenInfo = tokenStore.get(userName) ;
        if(tokenInfo == null || tokenInfo.getTokenExpiry() == null || Instant.now().isAfter(tokenInfo.getTokenExpiry().minusSeconds(60))){
            tokenInfo = refreshToken(loginRequest);
            tokenStore.put(userName , tokenInfo) ;
        }
        return tokenInfo.getCachedToken() ;
    }

    @Override
    public TokenInfo refreshToken(LoginRequest loginRequest) {
        String username = loginRequest.getUserName();
        TokenInfo tokenInfo = tokenStore.get(username);

        if (tokenInfo != null && tokenInfo.getRefreshToken() != null && Instant.now().isBefore(tokenInfo.getRefreshTokenExpiry())) {
            UserRefreshTokenExchangeParam param = UserRefreshTokenExchangeParam.builder()
                    .grant_type("refresh_token")
                    .client_id(idpProperties.getClientId())
                    .client_secret(idpProperties.getClientSecret())
                    .refresh_token(tokenInfo.getRefreshToken())
                    .build();

            UserTokenExchangeResponse response = identityProviderRepo.exchangeUserRefreshToken(idpProperties.getRealm(), param);
            return new TokenInfo(
                    response.getAccessToken(),
                    Instant.now().plusSeconds(Long.parseLong(response.getExpiresIn())),
                    response.getRefreshToken(),
                    Instant.now().plusSeconds(Long.parseLong(response.getRefreshExpiresIn()))
            );
        } else {
            UserAccessTokenExchangeParam param = UserAccessTokenExchangeParam.builder()
                    .grant_type("password")
                    .client_id(idpProperties.getClientId())
                    .client_secret(idpProperties.getClientSecret())
                    .username(username)
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
    }



}
