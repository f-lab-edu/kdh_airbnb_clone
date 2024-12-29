package com.airbnb_clone.backend;

import com.airbnb_clone.backend.common.CommonException;
import com.airbnb_clone.backend.common.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CommonException.class)
    public ResponseEntity exceptionHandler(CommonException e) {
        e.printStackTrace();

        return ResponseEntity.status(e.getErrorType().getHttpCode()).body(
        new CommonResponse(e.getErrorType().getCode(), e.getErrorType().getMessage(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<Object>> handleGenericException(Exception e) {
        e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CommonResponse<>(500, "An unexpected error occurred.", null));
    }
}
