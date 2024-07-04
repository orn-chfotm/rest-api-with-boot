package com.learn.restapiwithboot.core.enums;

import com.learn.restapiwithboot.core.exceptions.BaseException;
import com.learn.restapiwithboot.core.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ExceptionType {
    RESOURCE_NOT_FOUND(new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND)),
    TokenInvalidException(new ResourceNotFoundException(ErrorMessage.INVALID_JWT_TOKEN)),
    ;

    private final BaseException exception;


}
