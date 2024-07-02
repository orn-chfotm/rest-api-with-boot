package com.learn.restapiwithboot.config.filter;

import com.learn.restapiwithboot.config.provider.JwtTokenProvider;
import com.learn.restapiwithboot.config.provider.JwtAuthenticationToken;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManager authenticationManager;

    private static final String HEADER_AUTHORIZATION = "Authorization";

    private static final String BEARER_PREFIX = "Bearer ";

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
                                   AuthenticationManager authenticationManager) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = getJwtFromReqeust(request);

        try {
            if (jwtToken != null && jwtTokenProvider.validateToken(jwtToken)) {
                JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(jwtToken);
                Authentication authentication = authenticationManager.authenticate(jwtAuthenticationToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            request.setAttribute("exception", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("error", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromReqeust(HttpServletRequest request) {
        String authorization = request.getHeader(HEADER_AUTHORIZATION);
        if (authorization != null && authorization.startsWith(BEARER_PREFIX)) {
            return authorization.substring(BEARER_PREFIX.length());
        }

        log.warn("Request Header Authorization is not valid.");
        return null;
    }
}
