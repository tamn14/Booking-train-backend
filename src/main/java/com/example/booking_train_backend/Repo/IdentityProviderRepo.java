package com.example.booking_train_backend.Repo;
//----------------- REPO THUC HIEN THAO TAC VOI KEYCLOAK ----------------------- //

import com.example.booking_train_backend.DTO.KeycloakResponse.ClientTokenExchangeResponse;
import com.example.booking_train_backend.DTO.KeycloakResponse.RoleResponse;
import com.example.booking_train_backend.DTO.KeycloakResponse.UserTokenExchangeResponse;
import com.example.booking_train_backend.DTO.KeyloakRequest.*;
import com.example.booking_train_backend.DTO.Request.UsersUpdateRequest;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@FeignClient(name = "identity-keycloak-client", url = "${idp.url}")
public interface IdentityProviderRepo {

    @PostMapping(
            value = "/realms/{realm}/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    ) ClientTokenExchangeResponse exchangeClientToken (
            @PathVariable("realm") String realm ,
            @QueryMap ClientTokentExchangeParam clientTokentExchangeParam
    ) ;

    @PostMapping(
            value = "/realms/{realm}/protocol/openid-connect/logout",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    ) void revokeUserToken (
            @PathVariable("realm") String realm ,
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("refresh_token") String refreshToken
    ) ;

    @PostMapping(
            value = "/realms/{realm}/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    UserTokenExchangeResponse exchangeUserAccessToken(
            @PathVariable("realm") String realm ,
            @QueryMap UserAccessTokenExchangeParam tokenExchangeParam
    );

    @PostMapping(
            value = "/realms/{realm}/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    UserTokenExchangeResponse exchangeUserRefreshToken(
            @PathVariable("realm") String realm ,
            @QueryMap UserRefreshTokenExchangeParam tokenExchangeParam);

    @PostMapping(
            value = "/admin/realms/{realm}/users",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createUser(
            @PathVariable("realm") String realm ,
            @RequestHeader("Authorization") String token,
            @RequestBody UserCreationParam userCreationParam);

    @PutMapping("/admin/realms/{realm}/users/{id}/reset-password")
    void resetUserPassword(
            @PathVariable("realm") String realm,
            @RequestHeader("Authorization") String bearerToken,
            @PathVariable("id") String userId,
            @RequestBody Credential credential
    );

    @PutMapping("/admin/realms/{realm}/users/{id}")
    void updateUser( @PathVariable("realm") String realm,
                     @RequestHeader("Authorization") String bearerToken,
                     @PathVariable("id") String userId,
                     @RequestBody UsersUpdateRequest request
    );

    @GetMapping("/admin/realms/{realm}/roles/{roleName}")
    RoleResponse getRealmRoleByName(
            @PathVariable("realm") String realm,
            @RequestHeader("Authorization") String bearerToken,
            @PathVariable("roleName") String roleName
    );

    @PostMapping("/admin/realms/{realm}/users/{userId}/role-mappings/realm")
    void assignRealmRolesToUser(
            @PathVariable("realm") String realm,
            @RequestHeader("Authorization") String bearerToken,
            @PathVariable("userId") String userId,
            @RequestBody List<RoleResponse> roles
    );





}
