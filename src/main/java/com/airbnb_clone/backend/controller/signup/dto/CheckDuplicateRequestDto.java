package com.airbnb_clone.backend.controller.signup.dto;

import lombok.Getter;

@Getter
public class CheckDuplicateRequestDto {
    private String phoneNumber;
}