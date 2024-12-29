package com.airbnb_clone.backend.common;

import lombok.Getter;
import java.util.Map;

@Getter
public class CommonException extends RuntimeException {
    private final ErrorType errorType;
    private final Map<String, Object> parameter;

    public CommonException(ErrorType errorType, Map<String, Object> parameter) {
        this.errorType = errorType;
        this.parameter = parameter;
    }
}
