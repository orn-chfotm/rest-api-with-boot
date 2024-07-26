package com.learn.restapiwithboot.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static javax.servlet.http.HttpServletResponse.SC_OK;

@RequiredArgsConstructor
@Component
public class HandlerUtil {

    private final ObjectMapper objectMapper;

    public <T> String getResponseBody(T data) throws IOException {
        return objectMapper.writeValueAsString(data);
    }

    public void setResponse(HttpServletResponse response, int status) {
        response.setStatus(status);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

}
