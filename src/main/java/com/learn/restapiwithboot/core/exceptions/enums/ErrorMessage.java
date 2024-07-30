package com.learn.restapiwithboot.core.exceptions.enums;

public enum ErrorMessage {

    INVALID_INPUT_VALUE(400, "Invalid Input Value"),
    INVALID_JWT_TOKEN(401, "Invalid JWT Token"),
    INVALID_REFRESH_TOKEN(401, "Invalid Refresh Token"),
    BAD_CREDENTIALS(401, "BadCredentials::Authentication Failed"),
    ACCOUNT_NOT_FOUND(401, "Account information not found"),
    NOT_FOUND(404, "Not Found"),
    RESOURCE_NOT_FOUND(404, "Resource Not Found"),
    RESOURCE_MEETING_NOT_FOUND(404, "There is no such meeting."),
    RESOURCE_RESERVATION_NOT_FOUND(404, "There is no such reservation."),
    ACCOUNT_WITHDRAWAL(409, "Account is already withdrawal"),
    ACCOUNT_EXIST(409, "Account is already exist"),
    APPLICANT_MAX(409, "Reservation is full"),
    APPLICANT_MIN(409, "Reservation is minimum"),
    INTERNAL_SERVER_ERROR(500, "Server Error")
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
