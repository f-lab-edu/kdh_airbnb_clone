package com.airbnb_clone.backend.controller.common;

public record CommonResponse<T>(int code, String message, T payload) {
    public static <T> CommonResponse<T> success(T payload) {
        return new CommonResponse<>(200, null, payload);
    }
    public static CommonResponse<String> failed(String failedMsg) {
        return new CommonResponse<>(0, null, failedMsg);
    }
}

