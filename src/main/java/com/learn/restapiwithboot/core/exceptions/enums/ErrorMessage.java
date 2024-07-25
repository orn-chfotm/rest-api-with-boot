package com.learn.restapiwithboot.core.exceptions.enums;

public enum ErrorMessage {

    INVALID_INPUT_VALUE(400, "Invalid Input Value"),
    INVALID_JWT_TOKEN(401, "Invalid JWT Token"),
    INVALID_REFRESH_TOKEN(401, "Invalid Refresh Token"),
    INVALID_JWT_KEY(401, "Invalid JWT Key"),
    EXPIRED_TOKEN(401, "Expired Token"),
    BAD_CREDENTIALS(401, "BadCredentials::Authentication Failed"),
    NOT_FOUND(404, "Not Found"),
    RESOURCE_NOT_FOUND(404, "Resource Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    INTERNAL_SERVER_ERROR(500, "Server Error"),
    ACCOUNT_EXIST(409, "Account is already exist")
    ;

    /* Default status */
    private final Integer status;

    /* Default Message */
    private final String message;

    ErrorMessage(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public Integer getStatus() {
        return this.status;
    }
    public String getMessage() {
        return message;
    }
}