package com.learn.restapiwithboot.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.restapiwithboot.auth.dto.request.AuthRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (MediaType.APPLICATION_JSON_VALUE.equals(request.getContentType()) && HttpMethod.POST.toString().equals(request.getMethod())) {
            try {
                if (request.getInputStream().available() == 0) {
                    throw new AuthenticationException("Request body is empty") {};
                }
                AuthRequest authRequest = objectMapper.readValue(request.getInputStream(), AuthRequest.class);
                if (authRequest.getEmail() == null || authRequest.getPassword() == null) {
                    throw new AuthenticationException("Email or password is null") {};
                }

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                );
                super.setDetails(request, authenticationToken);
                return this.getAuthenticationManager().authenticate(authenticationToken);
            } catch (IOException e) {
                throw new AuthenticationException("Failed to parse request body", e) {};
            }
        } else {
            return super.attemptAuthentication(request, response);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        getFailureHandler().onAuthenticationFailure(request, response, failed);
    }
}
