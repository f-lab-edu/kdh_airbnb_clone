package com.airbnb_clone.backend.controller.signup;

import com.airbnb_clone.backend.common.CommonException;
import com.airbnb_clone.backend.common.CommonResponse;
import com.airbnb_clone.backend.common.ErrorType;
import com.airbnb_clone.backend.controller.signup.dto.CheckDuplicateRequestDto;
import com.airbnb_clone.backend.controller.signup.dto.SubmitFormRequestDto;
import com.airbnb_clone.backend.service.signup.SignupService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class SignupController {
    private final SignupService signupService;

    public SignupController(SignupService signupService) {
        this.signupService = signupService;
    }

    @PostMapping("/signup/checkDuplicate")
    public CommonResponse<String> checkDuplicate(@RequestBody CheckDuplicateRequestDto requestDto) {
        log.info("checkDuplicate. request : " + requestDto);
        String phoneNumber = requestDto.getPhoneNumber();
        if (!phoneNumber.matches("^010\\d{8}$")) {
            throw new CommonException(ErrorType.CHECK_DUPLICATE_INVALID_PHONE_NUMBER, null);
        }

        String result = signupService.checkDuplicate(requestDto.getPhoneNumber());
        return CommonResponse.success(result);
    }

    @PostMapping("/signup/submitForm")
    public CommonResponse<String> submitForm(@RequestBody SubmitFormRequestDto requestDto) {
        log.info("submitForm. request : " + requestDto);
        String phoneNumber = requestDto.getPhoneNumber();
        String name = requestDto.getName();
        String password = requestDto.getPassword();
        String passwordConfirm = requestDto.getPasswordConfirm();
        String birthDate = requestDto.getBirthDate();

        // check phone number
        if (!phoneNumber.matches("^010\\d{8}$")) {
            throw new CommonException(ErrorType.SIGNUP_WRONG_PHONE_NUMBER, null);
        }

        // check name
        if (
                name == null || name.length() < 2 || name.length() > 20 ||
                        !Pattern.matches("^[a-zA-Z가-힣\\s\\-.]+$", name)
        ) {
            throw new CommonException(ErrorType.SIGNUP_INVALID_NAME, null);
        }

        // check password
        if (password == null || password.length() < 6 || password.length() > 20) {
            throw new CommonException(ErrorType.SIGNUP_INVALID_PASSWORD, null);
        }

        final List<String> WEAK_PASSWORDS = Arrays.asList("password", "123456", "12345678", "qwerty", "abc123");
        if (WEAK_PASSWORDS.contains(password.toLowerCase())) {
            throw new CommonException(ErrorType.SIGNUP_INVALID_PASSWORD, null);
        }

        String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!*]).{6,20}$";
        if (!Pattern.matches(passwordRegex, password)) {
            throw new CommonException(ErrorType.SIGNUP_INVALID_PASSWORD, null);
        }

        if (!password.equals(passwordConfirm)) {
            throw new CommonException(ErrorType.SIGNUP_PASSWORD_NOT_MATCH, null);
        }

        // check birthdate
        if (birthDate == null || birthDate.isEmpty()) {
            throw new CommonException(ErrorType.SIGNUP_INVALID_BIRTHDATE, null);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedDate = LocalDate.parse(birthDate, formatter);

        if (parsedDate.isAfter(LocalDate.now())) {
            throw new CommonException(ErrorType.SIGNUP_INVALID_BIRTHDATE, null);
        }

        // Check if the user is at least 18 years old
        int age = Period.between(parsedDate, LocalDate.now()).getYears();
        if (age <= 18) {
            throw new CommonException(ErrorType.SIGNUP_AGE_NOT_QUALIFIED, null);
        }

        String result = signupService.submitForm(requestDto);
        return CommonResponse.success(result);
    }
}
