package com.example.booking_train_backend.Service.ServiceInterfaceImplement;

import com.example.booking_train_backend.DTO.KeycloakResponse.ClientTokenExchangeResponse;
import com.example.booking_train_backend.DTO.KeyloakRequest.ClientTokentExchangeParam;
import com.example.booking_train_backend.Properties.IdpProperties;
import com.example.booking_train_backend.Repo.IdentityProviderRepo;
import com.example.booking_train_backend.Service.ServiceInterface.KeycloakClientTokenService;
import com.example.booking_train_backend.exception.AppException;
import com.example.booking_train_backend.exception.ErrorCode;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
@Service
@Transactional
public class KeycloakClientTokenServiceImplement implements KeycloakClientTokenService {
    private IdpProperties idpProperties ;
    private IdentityProviderRepo identityProviderRepo ;
    @Autowired
    public KeycloakClientTokenServiceImplement(IdpProperties idpProperties,
                                               IdentityProviderRepo identityProviderRepo) {
        this.idpProperties = idpProperties;
        this.identityProviderRepo = identityProviderRepo;
    }

    private String cachedToken  ; // luu lai accessToken da lay duoc tu keycloak
    private Instant tokenExpiry ; // thoi gian het han cua token, muc dich nhan biet token da het han chua
    @Override
    public synchronized String getAccessToken () {
        // kiem tra neu token chua co hoac thoi gian het han con 60 giay nua se toi => refreshToken
        if(cachedToken == null || tokenExpiry == null || Instant.now().isAfter(tokenExpiry.minusSeconds(60))) {
            refreshToken();
        }
        return cachedToken ;
    }


    @Override
    public void refreshToken() {
        ClientTokentExchangeParam clientTokentExchangeParam = ClientTokentExchangeParam.builder()
                .grant_type("client_credentials") // gia tri mac dinh cua keycloak
                .client_id(idpProperties.getClientId())
                .client_secret(idpProperties.getClientSecret())
                .scope("openid")
                .build() ;
        ClientTokenExchangeResponse response = identityProviderRepo.exchangeClientToken(
                                                                                        idpProperties.getRealm(),
                                                                                        clientTokentExchangeParam) ;

        if (response == null || response.getAccessToken() == null) {
            throw new AppException(ErrorCode.KEYCLOAK_SERVICE_FAILED);
        }
        this.cachedToken = response.getAccessToken() ;
        this.tokenExpiry = Instant.now().plusSeconds(Long.parseLong(response.getExpiresIn())) ;;
    }
}
