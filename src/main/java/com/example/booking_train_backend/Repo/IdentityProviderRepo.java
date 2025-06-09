package com.example.booking_train_backend.Repo;
//----------------- REPO THUC HIEN THAO TAC VOI KEYCLOAK ----------------------- //

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "identity-keycloak-client", url = "${idp.url}")
public interface IdentityProviderRepo {

}
