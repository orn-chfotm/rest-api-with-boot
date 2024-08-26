package com.learn.restapiwithboot.config.handler;

import com.learn.restapiwithboot.core.dto.response.FailResponse;
import com.learn.restapiwithboot.core.util.HttpServletUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final HttpServletUtils httpServletUtils;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.warn("Login Fail : {}", exception.getMessage());
        FailResponse<Object> failResponse = new FailResponse<>(HttpStatus.UNAUTHORIZED.value(), exception.getMessage());

        httpServletUtils.setHandlerResponse(HttpStatus.UNAUTHORIZED, failResponse);
    }
}
