package com.airbnb_clone.backend.controller.signinout;

import com.airbnb_clone.backend.common.CommonException;
import com.airbnb_clone.backend.common.CommonResponse;
import com.airbnb_clone.backend.common.ErrorType;
import com.airbnb_clone.backend.controller.signinout.dto.SigninRequestDto;
import com.airbnb_clone.backend.controller.signinout.dto.SigninResponseDto;
import com.airbnb_clone.backend.controller.signinout.dto.SignoutRequestDto;
import com.airbnb_clone.backend.service.signin.SigninoutService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class SigninoutController {
    private final SigninoutService signinoutService;

    public SigninoutController(SigninoutService signinoutService) {
        this.signinoutService = signinoutService;
    }

    @PostMapping("/signin")
    public CommonResponse<SigninResponseDto> signin(@RequestBody SigninRequestDto requestDto) {
        log.info("signin. request : " + requestDto);
        String phoneNumber = requestDto.getPhoneNumber();
        String password = requestDto.getPassword();
        log.info("phoneNumber1 : " + phoneNumber);
        log.info("password1 : " + password);

        if (!phoneNumber.matches("^010\\d{8}$")) {
            throw new CommonException(ErrorType.SIGNIN_INVALID_PHONE_NUMBER, null);
        }

        // check password
        if (password.length() < 8 || password.length() > 64) {
            throw new CommonException(ErrorType.SIGNIN_INVALID_PASSWORD, null);
        }

        SigninResponseDto responseDto = signinoutService.signin(requestDto);
        return CommonResponse.success(responseDto);
    }

    @PostMapping("/signout")
    public CommonResponse<String> signout(@RequestBody SignoutRequestDto requestDto) {
        log.info("signout. request : " + requestDto);
        String accessToken = requestDto.accessToken();
        String refreshToken = requestDto.refreshToken();
        log.info("accessToken : " + accessToken);
        log.info("refreshToken : " + refreshToken);
        if (accessToken == null || refreshToken == null) {
            throw new CommonException(ErrorType.SIGNOUT_NO_TOKEN_IN_REQUEST, null);
        }
        String result = signinoutService.signout(requestDto);
        return CommonResponse.success(result);
    }
}
