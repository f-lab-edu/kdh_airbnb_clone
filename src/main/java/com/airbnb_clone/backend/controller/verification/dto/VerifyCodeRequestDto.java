package com.airbnb_clone.backend.controller.verification.dto;

import lombok.Getter;

@Getter
public class VerifyCodeRequestDto {
    private String encryptedKey;
    private String phoneNumber;
    private String verificationCode;
}
