package com.airbnb_clone.backend.controller.signinout.dto;

import lombok.Getter;

@Getter
public class SigninRequestDto {
    private String phoneNumber;
    private String password;
}
