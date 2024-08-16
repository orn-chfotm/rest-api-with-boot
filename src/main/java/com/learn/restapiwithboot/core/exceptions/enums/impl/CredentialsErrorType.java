package com.learn.restapiwithboot.core.exceptions.enums.impl;

import com.learn.restapiwithboot.core.exceptions.enums.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CredentialsErrorType implements ErrorType {

    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "Invalid Input Value"),
    INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid JWT Token"),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid Refresh Token"),
    BAD_CREDENTIALS(HttpStatus.UNAUTHORIZED, "BadCredentials::Authentication Failed");

    private final HttpStatus status;
    private final String message;
}
