package com.learn.restapiwithboot.core.exceptions.enums;

import com.learn.restapiwithboot.core.exceptions.BadCredentialsException;
import com.learn.restapiwithboot.core.exceptions.base.BaseException;
import com.learn.restapiwithboot.core.exceptions.ResourceNotFoundException;
import com.learn.restapiwithboot.core.exceptions.TokenInvalidException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ExceptionType {
    RESOURCE_NOT_FOUND(new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND)),
    TOKENINVALID_EXCEPTION(new TokenInvalidException(ErrorMessage.INVALID_JWT_TOKEN)),
    BADCREDENTIALSEXCETPION(new BadCredentialsException(ErrorMessage.BAD_CREDENTIALS)),
    ;

    private final BaseException exception;

}
