package com.airbnb_clone.backend.service.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

// Seems getter and setter doesn't work. don't know why -_-;
@Getter
@Setter
public class VerifyCodeRequestDto {
    private String encryptedKey;
    private String phoneNumber;
    private String verificationCode;

    public String getEncryptedKey() {
        return this.encryptedKey;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getVerificationCode() {
        return this.verificationCode;
    }
}
