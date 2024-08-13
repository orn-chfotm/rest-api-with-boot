package com.learn.restapiwithboot.core.exceptions.enums.impl;

import com.learn.restapiwithboot.core.exceptions.enums.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorType implements ErrorType {

    BAD_REQEUST(HttpStatus.BAD_REQUEST, "Bad Request"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "Not Found"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");

    private final HttpStatus status;
    private final String message;
}
