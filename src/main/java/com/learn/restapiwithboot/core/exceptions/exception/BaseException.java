package com.learn.restapiwithboot.core.exceptions.exception;

import com.learn.restapiwithboot.core.exceptions.enums.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class BaseException extends RuntimeException {

    private final ErrorType errorType;

    public HttpStatus getStatus() {
        return errorType.getStatus();
    }

    public String getMessage() {
        return errorType.getMessage();
    }

}
