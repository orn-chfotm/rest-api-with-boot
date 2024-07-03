package com.learn.restapiwithboot.core.enums;

import com.learn.restapiwithboot.core.exceptions.BaseException;
import com.learn.restapiwithboot.core.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NewExceptions {
    RESOURCE_NOT_FOUND(new ResourceNotFoundException("Resource Not Found", 404)),
    NEWRESOURCE_NOT_FOUND(new ResourceNotFoundException("Resource Not Found", 404)),
    ;

    private final BaseException exception;


}
