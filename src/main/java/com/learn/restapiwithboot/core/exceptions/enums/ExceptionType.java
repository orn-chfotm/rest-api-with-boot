package com.learn.restapiwithboot.core.exceptions.enums;

import com.learn.restapiwithboot.core.exceptions.impl.BadCredentialsException;
import com.learn.restapiwithboot.core.exceptions.BaseException;
import com.learn.restapiwithboot.core.exceptions.impl.ResourceNotFoundException;
import com.learn.restapiwithboot.core.exceptions.impl.TokenInvalidException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ExceptionType {
    RESOURCE_NOT_FOUND(new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND)),
    TOKENINVALID_EXCEPTION(new TokenInvalidException(ErrorMessage.INVALID_JWT_TOKEN)),
    BADCREDENTIALSEXCETPION(new BadCredentialsException(ErrorMessage.BAD_CREDENTIALS)),
    ;

    private final BaseException exception;

}
