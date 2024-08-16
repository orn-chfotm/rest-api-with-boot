package com.learn.restapiwithboot.core.exceptions.enums.impl;

import com.learn.restapiwithboot.core.exceptions.enums.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResourceErrorType implements ErrorType {

    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource Not Found"),
    RESOURCE_MEETING_NOT_FOUND(HttpStatus.NOT_FOUND, "There is no such meeting."),
    RESOURCE_RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "There is no such reservation."),
    APPLICANT_ALREADY(HttpStatus.CONFLICT, "It's already reserved."),
    APPLICANT_MAX(HttpStatus.CONFLICT, "Reservation is full"),
    APPLICANT_MIN(HttpStatus.CONFLICT, "Reservation is minimum");

    private final HttpStatus status;
    private final String message;
}
