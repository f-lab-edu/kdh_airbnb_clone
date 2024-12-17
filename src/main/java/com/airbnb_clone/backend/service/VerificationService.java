package com.airbnb_clone.backend.service;

import com.airbnb_clone.backend.controller.common.CommonResponse;
import com.airbnb_clone.backend.controller.verification.dto.GeneratedCodeResponse;
import com.airbnb_clone.backend.cryptoKey.JwtKey;
import com.airbnb_clone.backend.service.dto.VerifyCodeRequestDto;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class VerificationService {
    private JwtKey jwtKey;  // Jwt token used as an encrypted key for each verification process

    public GeneratedCodeResponse generateCode(String phoneNum) {
        Random random = new Random();
        int number = 100000 + random.nextInt(900000); // Ensure 6 digits
        String code = String.valueOf(number);
        jwtKey = new JwtKey(phoneNum, code);

        // has to return both jwtKey and code at once with DTO.
        return new GeneratedCodeResponse(jwtKey.getKey(), code);
    }

    public CommonResponse<String> verifyCode(VerifyCodeRequestDto dto) {
        String encryptedKey = dto.getEncryptedKey();
        String phoneNumFromJwt = jwtKey.getPhoneNum(encryptedKey);
        String codeFromJwt = jwtKey.getCode(encryptedKey);

        System.out.println("dto phoneNum : " + dto.getPhoneNumber());
        System.out.println("dto phoneNum type : " + dto.getPhoneNumber());
        System.out.println("dto code : " + dto.getVerificationCode());

        System.out.println("jwt phoneNum : " + phoneNumFromJwt);
        System.out.println("jwt code : " + codeFromJwt);

        // when token has been expired or token is invalid
        if (phoneNumFromJwt == null || codeFromJwt == null) {
            System.out.println("Invalid JWT key");
            return CommonResponse.failed("Invalid JWT Key");
        }
        if (!phoneNumFromJwt.equals(dto.getPhoneNumber())) {
            System.out.println("wrong phoneNumber");
            return CommonResponse.failed("wrong phoneNumber");
        }
        if (!codeFromJwt.equals(dto.getVerificationCode())) {
            System.out.println("wrong verification code");
            return CommonResponse.failed("wrong verification code");
        }
        // return success when token is valid and meets all conditions.
        jwtKey = null;
        return CommonResponse.success("Verified");
    }
}
