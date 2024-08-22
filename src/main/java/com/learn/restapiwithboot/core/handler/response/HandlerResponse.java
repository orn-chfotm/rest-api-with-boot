package com.learn.restapiwithboot.core.handler.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@Component
public class HandlerResponse {

    private final ObjectMapper objectMapper;

    public <T> void setHandlerResponse(HttpStatus status, T responseBody) throws IOException {
        ServletWebRequest requestAttributes = (ServletWebRequest) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) throw new RuntimeException("ServletWebRequest is null");

        HttpServletResponse response = requestAttributes.getResponse();
        if (response == null) throw new RuntimeException("response is null");

        response.setStatus(status.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }
}
