package com.example.booking_train_backend.DTO.KeycloakRequest;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoogleTokenExchangeParam {
    private String grant_type;
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String scope ;
}
