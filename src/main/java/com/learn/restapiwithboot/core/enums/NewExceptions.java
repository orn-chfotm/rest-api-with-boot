package com.learn.restapiwithboot.core.enums;

import com.learn.restapiwithboot.core.exceptions.BaseException;
import com.learn.restapiwithboot.core.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NewExceptions {

    INVALID_INPUT_VALUE(400, "Invalid Input Value"),
    INVALID_TYPE_VALUE(400, "Invalid Type Value"),
    INVALID_INPUT_FORMAT(400, "Invalid Input Format"),
    INVALID_JWT_TOKEN(401, "Invalid JWT Token"),
    INVALID_REFRESH_TOKEN(401, "Invalid Refresh Token"),
    INVALID_JWT_KEY(401, "Invalid JWT Key"),
    EXPIRED_TOKEN(401, "Expired Token"),
    NOT_FOUND(404, "Not Found"),
    RESOURCE_NOT_FOUND(new ResourceNotFoundException("Resource Not Found", 404)),
    NEWRESOURCE_NOT_FOUND(new ResourceNotFoundException("Resource Not Found", 404)),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    INTERNAL_SERVER_ERROR(500, "Server Error")
    ;

    private final BaseException exception;


}
