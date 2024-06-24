package com.learn.restapiwithboot.common.dto.response;

import com.learn.restapiwithboot.common.dto.BasicResponse;
import lombok.Getter;

@Getter
public class FailResponse extends BasicResponse {

    private String detailMessage;

    public FailResponse(Integer statusCode, String exceptionMessage) {
        super(statusCode, exceptionMessage);
    }

    public FailResponse(Integer statusCode, String exceptionMessage, String detailMessage) {
        super(statusCode, exceptionMessage);
        this.detailMessage = detailMessage;
    }
}
