package com.learn.restapiwithboot.config.filter;

import com.learn.restapiwithboot.core.exceptions.TokenExpiredException;
import com.learn.restapiwithboot.core.exceptions.TokenInvalidException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            throw new TokenInvalidException("JwtExceptionFilter - Token Error");
        }
    }
}
