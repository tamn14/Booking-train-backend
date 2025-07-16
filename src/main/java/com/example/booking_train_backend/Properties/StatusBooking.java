package com.example.booking_train_backend.Properties;

public enum StatusBooking {

    PENDING("PENDING"),
    COMPLETED("COMPLETED") ,
    CANCELLED ("CANCELLED")    ;


    private final String value;

    StatusBooking(String value) {
        this.value = value;
    }


    public String getValue() {
        return value;
    }
}
