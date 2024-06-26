package com.learn.restapiwithboot.common.dto.response;

import com.learn.restapiwithboot.common.dto.BasicResponse;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class FailResponse extends BasicResponse {

    private Map<String, String> errMessageMap;

    public FailResponse(Integer statusCode, String exceptionMessage) {
        super(statusCode, exceptionMessage);
    }

    public FailResponse(Integer statusCode, String message, Map<String, String> errMessageMap) {
        super(statusCode, message);
        this.errMessageMap = errMessageMap;
    }
}
