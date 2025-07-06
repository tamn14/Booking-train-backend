package com.example.booking_train_backend.Properties;

public enum RoleTemplate {
    USER("USER"),
    ADMIN("ADMIN");

    private final String value;

    RoleTemplate(String value) {
        this.value = value;
    }


    public String getValue() {
        return value;
    }
}
