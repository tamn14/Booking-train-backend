package com.example.booking_train_backend.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class KeycloakNormalizer {
    private ObjectMapper objectMapper; // chuyen doi json thanh doi tuong
    private Map<String , ErrorCode> errorCodeMap ; // anh xa loi keycloak sang ma noi bo Errorcode

    public KeycloakNormalizer() {
        objectMapper = new ObjectMapper();
        errorCodeMap = new HashMap<>();

        errorCodeMap.put("User exists with same username", ErrorCode.USER_EXISTED);
        errorCodeMap.put("User exists with same email", ErrorCode.EMAIL_EXISTED);
        errorCodeMap.put("User name is missing", ErrorCode.USERNAME_IS_MISSING);
    }

    public AppException handleKeycloakException (FeignException feignException) {
        try {
            // ghi log canh bao
            log.warn(" Cannot complete request : " , feignException);
            // lay noi dung loi tra ve
            var response = objectMapper.readValue(feignException.contentUTF8() , KeycloakError.class ) ;
            // kiem tra loi co nam trong danh sach loi co the xu ly
            if(Objects.nonNull(response.getMessage()) && Objects.nonNull(errorCodeMap.get(response.getMessage()))) {
                return new AppException(errorCodeMap.get(response.getMessage()));
            }

        }catch (JsonProcessingException e) {
            log.error("Cannot deserialize content: ", e);
        }

        return new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
    }



}
