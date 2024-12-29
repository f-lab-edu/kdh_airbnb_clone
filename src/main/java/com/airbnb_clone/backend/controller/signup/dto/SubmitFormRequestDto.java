package com.airbnb_clone.backend.controller.signup.dto;

import lombok.Getter;

@Getter
public class SubmitFormRequestDto {
    private String phoneNumber;
    private String name;
    private String password;
    private String passwordConfirm;
    private String birthDate;
}
