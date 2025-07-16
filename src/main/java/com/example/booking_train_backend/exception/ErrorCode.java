package com.example.booking_train_backend.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1002, "user not existed", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1003, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1004, "You do not have permission", HttpStatus.FORBIDDEN),
    BOOKING_NOT_FOUND(1005,"Booking not existed" ,HttpStatus.NOT_FOUND ),
    ACCOUNTS_BLOCK(1006,"Account has been block" ,HttpStatus.UNAUTHORIZED ),
    TRAIN_STATION_NOT_EXISTED(1007,"Train station not existed" ,  HttpStatus.NOT_FOUND),
    TRAIN_STATION_EXISTED(1017,"Train station  existed" ,  HttpStatus.CONFLICT),
    STATUS_NOT_EXISTED(1008,"Status not existed" ,  HttpStatus.NOT_FOUND),
    CARRIAGE_CLASS_NOT_EXISTED(1009,"Carriage class not existed" ,  HttpStatus.NOT_FOUND),
    CARRIAGE_PRICE_NOT_EXISTED(1014,"Carriage price not existed" ,  HttpStatus.NOT_FOUND),
    CARRIAGE_CLASS_EXISTED(1012,"Carriage class is existed" ,  HttpStatus.CONFLICT),
    SCHEDULE_NOT_EXISTED(1013,"Schedule not existed" ,  HttpStatus.NOT_FOUND),
    SCHEDULE_EXISTED(1011,"Schedule  existed" ,  HttpStatus.NOT_FOUND),
    USER_EXISTED(1015, "User already existed", HttpStatus.CONFLICT),
    EMAIL_EXISTED(1016, "Email already existed", HttpStatus.CONFLICT),
    USERNAME_IS_MISSING(1017, "Username is missing", HttpStatus.BAD_REQUEST),
    CARRIAGE_PRICE_ALREADY_EXISTS(1017, "CarriagePriceID already existed", HttpStatus.CONFLICT),
    JOURNEY_STATION_NOT_EXISTS(1018, "Journey Station not existed", HttpStatus.NOT_FOUND),
    JOURNEY_CARRIAGE_NOT_EXISTS(1019, "Journey Carriage not existed", HttpStatus.NOT_FOUND),
    TRAIN_TRIP_NOT_EXISTED(1020,"Train trip not existed" ,  HttpStatus.NOT_FOUND),
    KEYCLOAK_SERVICE_FAILED(1023, "Can not get token from Keycloak ", HttpStatus.BAD_REQUEST),
    EMAIL_SERVICE_FAILED(1024, "Can not send mail ", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST(1025, "Invalid request , can not find request ", HttpStatus.BAD_REQUEST),
    BOOKING_TIMEOUT_FOR_CANCEL(1026,"Can't find status booking " , HttpStatus.NOT_FOUND),
    BOOKING_STATUS_NOT_EXISTED(1021,"Can't scancel booking after 30 minute" ,  HttpStatus.BAD_REQUEST),
    CANNOT_CREATE_QR(1019,"cannot create QR ",HttpStatus.BAD_REQUEST ),
    TRAIN_JOURNEY_NOT_EXISTED(1010,"Train journey not existed" ,  HttpStatus.NOT_FOUND);

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
