package com.example.booking_train_backend.Properties;

public enum VietQrTemplate {
    COMPACT2("compact2"),
    CUSTOM("custom");

    private final String value;

    VietQrTemplate(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
