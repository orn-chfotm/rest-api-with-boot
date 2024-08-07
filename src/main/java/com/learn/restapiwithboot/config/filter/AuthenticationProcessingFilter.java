package com.learn.restapiwithboot.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.restapiwithboot.account.dto.request.AccountRequest;
import com.learn.restapiwithboot.auth.dto.request.AuthRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    public AuthenticationProcessingFilter(String defaultFilterProcessesUrl,
                                          ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher(defaultFilterProcessesUrl, HttpMethod.POST.toString()));
        this.objectMapper = objectMapper;
    }

    /**
     * 로그인 시 param 으로 전달되는 값 Dto 변환 처리
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (request.getInputStream().available() == 0) {
            throw new BadCredentialsException("Request body is empty");
        }

        AccountRequest accountRequest = objectMapper.readValue(request.getInputStream(), AccountRequest.class);

        if (accountRequest.getEmail() == null || accountRequest.getPassword() == null) {
            throw new BadCredentialsException("Email or password is null");
        }

        return this.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
                accountRequest.getEmail(),
                accountRequest.getPassword()
        ));
    }
}
