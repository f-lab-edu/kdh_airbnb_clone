package com.airbnb_clone.backend.controller.verification;

import com.airbnb_clone.backend.common.CommonResponse;
import com.airbnb_clone.backend.controller.verification.dto.GeneratedCodeResponse;
import com.airbnb_clone.backend.common.CommonException;
import com.airbnb_clone.backend.common.ErrorType;
import com.airbnb_clone.backend.cryptoKey.Jwt_verification;
import com.airbnb_clone.backend.service.verification.VerificationService;
import com.airbnb_clone.backend.controller.verification.dto.GenerateCodeRequestDto;
import com.airbnb_clone.backend.controller.verification.dto.VerifyCodeRequestDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
@RequiredArgsConstructor
public class VerificationController {
    private final VerificationService verificationService;

    @PostMapping("/verification/getCode")
    public CommonResponse<GeneratedCodeResponse> getVerificationCode(@RequestBody GenerateCodeRequestDto requestDto) {
        log.info("getVerificationCode. request : " + requestDto);
        String phoneNumber = requestDto.getPhoneNumber();
        if (!phoneNumber.matches("^010\\d{8}$")) {
            throw new CommonException(ErrorType.GENERATE_CODE_INVALID_PHONE_NUMBER, null);
        }
        else {
            GeneratedCodeResponse dto = verificationService.generateCode(requestDto.getPhoneNumber());
            // when return, response object must contain verification code and encrypted key
            return CommonResponse.success(dto);
        }
    }

    @PostMapping("/verification/verifyCode")
    public CommonResponse<String> verifyCode(@RequestBody VerifyCodeRequestDto dto) {
        log.info("[VerificationController]verifyCode. request : " + dto);
        //MDC 이용하기
        // 로그 너무 많이 출력하면 별로 안 좋음.
        // 에러 발생 시에만 로그 출력하는 게 좋음.
        log.info("dto phoneNum : " + dto.getPhoneNumber());
        log.info("dto phoneNum type : " + dto.getPhoneNumber());
        log.info("dto code : " + dto.getVerificationCode());

        // verifycoderequestDto 내에서 선언하면 코드가 더 간결해짐. interface 이용할 수 있음.
        String encryptedKey = dto.getEncryptedKey();
        String phoneNumFromJwt = Jwt_verification.getPhoneNum(encryptedKey);
        String codeFromJwt = Jwt_verification.getCode(encryptedKey);

        if (dto.getPhoneNumber() == null || dto.getVerificationCode() == null) {
            throw new CommonException(ErrorType.VERIFICATION_WRONG_PARAM, null);
        }
        // when token has been expired or token is invalid
        if (phoneNumFromJwt == null || codeFromJwt == null) {
            throw new CommonException(ErrorType.VERIFICATION_INVALID_JWT_KEY, null);
        }
        if (!phoneNumFromJwt.equals(dto.getPhoneNumber())) {
            throw new CommonException(ErrorType.VERIFICATION_WRONG_PHONENUM, null);
        }
        if (!codeFromJwt.equals(dto.getVerificationCode())) {
            throw new CommonException(ErrorType.VERIFICATION_WRONG_CODE, null);
        }


        // request object must contain code, encrypted key, phone number
        // request object is defined as a DTO and it is passed to a service for the next process.

        // when return, response object must contain success or failed code and message(optional).
        // response object is defined as the CommonResponse DTO.
        return verificationService.verifyCode(encryptedKey, phoneNumFromJwt);
    }
}

