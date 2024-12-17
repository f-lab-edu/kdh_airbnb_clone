package com.airbnb_clone.backend.service.dto;

public record GetVerificationCodeRequestDto(String phoneNumber) {
    public String getPhoneNumber() {
        return phoneNumber;
    }
}
