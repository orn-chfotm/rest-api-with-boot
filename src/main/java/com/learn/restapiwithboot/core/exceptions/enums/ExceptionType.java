package com.learn.restapiwithboot.core.exceptions.enums;

import com.learn.restapiwithboot.core.exceptions.impl.AccountExistenceException;
import com.learn.restapiwithboot.core.exceptions.impl.BadCredentialsException;
import com.learn.restapiwithboot.core.exceptions.BaseException;
import com.learn.restapiwithboot.core.exceptions.impl.ResourceNotFoundException;
import com.learn.restapiwithboot.core.exceptions.impl.TokenInvalidException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ExceptionType {
    RESOURCE_NOT_FOUND(new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND)),
    RESOURCE_MEETING_NOT_FOUND(new ResourceNotFoundException(ErrorMessage.RESOURCE_MEETING_NOT_FOUND)),
    RESOURCE_RESERVATION_NOT_FOUND(new ResourceNotFoundException(ErrorMessage.RESOURCE_RESERVATION_NOT_FOUND)),
    ACCOUNT_NOT_FOUND(new ResourceNotFoundException(ErrorMessage.ACCOUNT_NOT_FOUND)),
    INVALID_TOKEN_EXCEPTION(new TokenInvalidException(ErrorMessage.INVALID_JWT_TOKEN)),
    INVALID_REFRESH_TOKEN_EXCEPTION(new TokenInvalidException(ErrorMessage.INVALID_REFRESH_TOKEN)),
    BAD_CREDENTIALS_EXCETPION(new BadCredentialsException(ErrorMessage.BAD_CREDENTIALS)),
    ACCOUNT_EXIST_EXCEPTION(new AccountExistenceException(ErrorMessage.ACCOUNT_EXIST)),
    ACCOUNT_WITHDRAWAL_EXCEPTION(new AccountExistenceException(ErrorMessage.ACCOUNT_WITHDRAWAL))
    ;

    private final BaseException exception;

    public BaseException getException() {
        return exception;
    }
}
