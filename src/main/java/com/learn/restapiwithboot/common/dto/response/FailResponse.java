package com.learn.restapiwithboot.common.dto.response;

import com.learn.restapiwithboot.common.dto.BasicResponse;

public class FailResponse extends BasicResponse {

    public FailResponse(Integer statusCode, String message) {
        super(statusCode, message);
    }
}
