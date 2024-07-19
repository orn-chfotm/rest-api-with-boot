package com.learn.restapiwithboot.core.dto.response;

import com.learn.restapiwithboot.core.dto.BasicResponse;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class FailResponse<T> extends BasicResponse {

    private T errs;

    public FailResponse(Integer statusCode, String exceptionMessage) {
        super(statusCode, exceptionMessage);
    }

    public FailResponse(Integer statusCode, String message, T errs) {
        super(statusCode, message);
        this.errs = errs;
    }
}
