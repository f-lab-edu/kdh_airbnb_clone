package com.airbnb_clone.backend.controller.verification;

import com.airbnb_clone.backend.controller.common.CommonResponse;
import com.airbnb_clone.backend.controller.verification.dto.GeneratedCodeResponse;
import com.airbnb_clone.backend.service.VerificationService;
import com.airbnb_clone.backend.service.dto.GetVerificationCodeRequestDto;
import com.airbnb_clone.backend.service.dto.VerifyCodeRequestDto;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class VerificationController {
    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @PostMapping("/verification/getCode")
    public CommonResponse<GeneratedCodeResponse> getVerificationCode(@RequestBody GetVerificationCodeRequestDto requestDto) {
        System.out.println("[VerificationController]getVerificationCode. request : " + requestDto);
        GeneratedCodeResponse dto = verificationService.generateCode(requestDto.getPhoneNumber());

        // when return, response object must contain verification code and encrypted key
        return CommonResponse.success(dto);
    }

    @PostMapping("/verification/verifyCode")
    public CommonResponse<String> verifyCode(@RequestBody VerifyCodeRequestDto verifyCodeRequestDto) {
        System.out.println("[VerificationController]verifyCode");
        // request object must contain code, encrypted key, phone number
        // request object is defined as a DTO and it is passed to a service for the next process.

        // when return, response object must contain success or failed code and message(optional).
        // response object is defined as the CommonResponse DTO.
        return verificationService.verifyCode(verifyCodeRequestDto);
    }
}

