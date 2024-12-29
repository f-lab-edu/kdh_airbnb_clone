package com.airbnb_clone.backend.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {
    SIGNIN_INVALID_PHONE_NUMBER(10001, HttpStatus.CONFLICT, "올바른 전화번호를 입력해 주세요."),
    SIGNIN_INVALID_PASSWORD(10002, HttpStatus.CONFLICT, "올바른 비밀번호를 입력해 주세요."),
    SIGNIN_USER_NOT_FOUND(10003, HttpStatus.NOT_FOUND, "해당 번호로 가입된 계정이 없습니다."),
    SIGNIN_PASSWORD_NOT_MATCH(10004, HttpStatus.NOT_FOUND, "패스워드가 일치하지 않습니다"),
    SIGNOUT_NO_TOKEN_IN_REQUEST(10005, HttpStatus.CONFLICT, "토큰이 없습니다."),
    SIGNOUT_INVALID_TOKEN(10006, HttpStatus.NOT_FOUND, "유효하지 않은 토큰입니다."),

    GENERATE_CODE_INVALID_PHONE_NUMBER(20001, HttpStatus.BAD_REQUEST, "올바른 전화번호를 입력해 주세요."),
    GENERATE_CODE_COUNT_LIMIT_EXCEEDED(20002, HttpStatus.CONFLICT, "인증번호 전송 요청이 5회를 초과하였습니다. 24시간 후에 다시 시도해 주세요."),

    VERIFICATION_WRONG_PARAM(30001, HttpStatus.CONFLICT, "누락된 요청 파라미터가 있습니다."),
    VERIFICATION_INVALID_JWT_KEY(30002, HttpStatus.CONFLICT, "유효하지 않은 키값입니다."),
    VERIFICATION_WRONG_PHONENUM(30003, HttpStatus.CONFLICT, "잘못된 전화번호입니다."),
    VERIFICATION_WRONG_CODE(30004, HttpStatus.CONFLICT, "잘못된 인증코드입니다."),
    VERIFICATION_KEY_EXPIRED(30005, HttpStatus.CONFLICT, "인증시간이 지났습니다."),

    CHECK_DUPLICATE_INVALID_PHONE_NUMBER(40001, HttpStatus.CONFLICT, "잘못된 전화번호입니다."),
    CHECK_DUPLICATE_PHONE_NUMBER_EXPIRED(40002, HttpStatus.CONFLICT, "전화번호를 인증해 주세요"),

    SIGNUP_WRONG_PHONE_NUMBER(50001, HttpStatus.CONFLICT, "잘못된 전화번호입니다."),
    SIGNUP_ALREADY_SIGNED_UP(50002, HttpStatus.CONFLICT, "이미 가입된 전화번호입니다."),
    SIGNUP_INVALID_NAME(50003, HttpStatus.CONFLICT, "이름은 2자 이상 20자 미만이어야 하며 특수문자는 공백, 하이폰(-), 점(.)만 허용합니다."),
    SIGNUP_INVALID_PASSWORD(50004, HttpStatus.CONFLICT, "비밀번호는 6자 이상 14자 미만이어야 하며 대문자, 소문자, 숫자, 특수문자 중 최소 하나씩을 포함해야 합니다. 또한 다음과 같은 취약한 패스워드는 사용하지 않아야 합니다. \"password\", \"123456\", \"12345678\", \"qwerty\", \"abc123\""),
    SIGNUP_PASSWORD_NOT_MATCH(50005, HttpStatus.CONFLICT, "비밀번호와 비밀번호 확인값이 일치하지 않습니다"),
    SIGNUP_INVALID_BIRTHDATE(50006, HttpStatus.CONFLICT, "잘못된 생년월일입니다."),
    SIGNUP_AGE_NOT_QUALIFIED(50007, HttpStatus.CONFLICT, "18세 미만은 가입할 수 없습니다.");

    @Getter
    private final int code;
    private final HttpStatus httpCode;
    private final String message;

    ErrorType(int code, HttpStatus httpCode, String message) {
        this.code = code;
        this.httpCode = httpCode;
        this.message = message;
    }
}