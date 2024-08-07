package com.learn.restapiwithboot.core.exceptions.enums;

import com.learn.restapiwithboot.core.exceptions.impl.*;
import com.learn.restapiwithboot.core.exceptions.BaseException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ExceptionType {
    INVALID_TOKEN_EXCEPTION(new TokenInvalidException(ErrorMessage.INVALID_JWT_TOKEN)),
    INVALID_REFRESH_TOKEN_EXCEPTION(new TokenInvalidException(ErrorMessage.INVALID_REFRESH_TOKEN)),
    ACCOUNT_NOT_FOUND(new ResourceNotFoundException(ErrorMessage.ACCOUNT_NOT_FOUND)),
    BAD_CREDENTIALS_EXCEPTION(new BadCredentialsException(ErrorMessage.BAD_CREDENTIALS)),
    RESOURCE_NOT_FOUND(new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND)),
    RESOURCE_MEETING_NOT_FOUND(new ResourceNotFoundException(ErrorMessage.RESOURCE_MEETING_NOT_FOUND)),
    RESOURCE_RESERVATION_NOT_FOUND(new ResourceNotFoundException(ErrorMessage.RESOURCE_RESERVATION_NOT_FOUND)),
    ACCOUNT_EXIST_EXCEPTION(new AccountExistenceException(ErrorMessage.ACCOUNT_EXIST)),
    APPLICANT_ALREADY_EXCEPTION(new ApplicantCountException(ErrorMessage.APPLICANT_ALREADY)),
    APPLICANT_MAX_EXCEPTION(new ApplicantCountException(ErrorMessage.APPLICANT_MAX)),
    APPLICANT_MIN_EXCEPTION(new ApplicantCountException(ErrorMessage.APPLICANT_MIN)),
    ACCOUNT_WITHDRAWAL_EXCEPTION(new AccountExistenceException(ErrorMessage.ACCOUNT_WITHDRAWAL))
    ;

    private final BaseException exception;

    public BaseException getException() {
        return exception;
    }
}
