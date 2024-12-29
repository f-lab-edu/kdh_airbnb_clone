package com.airbnb_clone.backend.common;

public record CommonResponse<T>(int code, String message, T payload) {
    public static <T> CommonResponse<T> success(T payload) {
        return new CommonResponse<>(200, null, payload);
    }
}
