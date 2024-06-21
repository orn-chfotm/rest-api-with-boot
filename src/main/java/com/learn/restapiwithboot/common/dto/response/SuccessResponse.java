package com.learn.restapiwithboot.common.dto.response;

import com.learn.restapiwithboot.common.dto.BasicResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SuccessResponse<T> extends BasicResponse {

    private final T data;

    public SuccessResponse(Integer statusCode, String message, T data) {
        super(statusCode, message);
        this.data = data;
    }

    public static <T> ResponseEntity<?> of(T data) {
        final HttpStatus success = HttpStatus.OK;
        return new ResponseEntity<>(new SuccessResponse<>(success.value(), success.getReasonPhrase(), data), success);
    }
}
