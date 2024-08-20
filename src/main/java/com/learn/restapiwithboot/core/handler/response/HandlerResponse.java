package com.learn.restapiwithboot.core.handler.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.restapiwithboot.core.dto.response.FailResponse;
import com.learn.restapiwithboot.core.exceptions.enums.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class HandlerResponse {

    private final ObjectMapper objectMapper;

    public <T> void setHandlerResponse(HttpServletResponse response, HttpStatus status, T responseBody) throws IOException {
        response.setStatus(status.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }

    public ResponseEntity<FailResponse<Void>> getResponse(ErrorType errorType) {
        return ResponseEntity.status(errorType.getStatus()).body(
                new FailResponse<>(errorType.getStatus().value(), errorType.getMessage())
        );
    }

    public ResponseEntity<FailResponse<Void>> getResponse(Throwable exception, ErrorType errorType) {
        log.error("Handle [{}] with a message ::", exception.getClass().getName() ,exception);
        return ResponseEntity.status(errorType.getStatus()).body(
                new FailResponse<>(errorType.getStatus().value(), errorType.getMessage())
        );
    }

    public ResponseEntity<FailResponse<List<ValidationErrorResponse>>> getNotValidResponse(
            List<ValidationErrorResponse> notValidList, ErrorType errorType) {
        return ResponseEntity.badRequest().body(new FailResponse<>(
                errorType.getStatus().value(),
                errorType.getMessage(),
                notValidList
        ));
    }


}
