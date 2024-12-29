package com.airbnb_clone.backend.controller.signinout.dto;

public record SignoutRequestDto(String accessToken, String refreshToken) {}