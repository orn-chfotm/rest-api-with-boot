package com.learn.restapiwithboot.core.exceptions.enums.impl;

import com.learn.restapiwithboot.core.exceptions.enums.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AccountErrorType implements ErrorType {

    ACCOUNT_WITHDRAWAL(HttpStatus.GONE, "Account is already withdrawal"),
    ACCOUNT_EXIST(HttpStatus.CONFLICT, "Account is already exist"),
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "Account information not found");

    private final HttpStatus status;
    private final String message;
}
