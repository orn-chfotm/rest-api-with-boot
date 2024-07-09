package com.learn.restapiwithboot.config.filter;

import com.learn.restapiwithboot.config.authentication.JwtAuthenticationToken;
import com.learn.restapiwithboot.config.provider.JwtTokenProvider;
import com.learn.restapiwithboot.config.properties.JwtProperties;
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

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManager authenticationManager;

    private final JwtProperties jwtProperties;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
                                   AuthenticationManager authenticationManager,
                                   JwtProperties jwtProperties) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.jwtProperties = jwtProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = getJwtFromReqeust(request);

        try {
            if (jwtToken != null && jwtTokenProvider.validateToken(jwtToken, this.jwtProperties.getAccessSecretKey())) {
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
        String authorization = request.getHeader(jwtProperties.getHeader());
        if (authorization != null && authorization.startsWith(jwtProperties.getPrefix())) {
            return authorization.substring(jwtProperties.getPrefix().length());
        }

        log.warn("Request Header Authorization is not valid.");
        return null;
    }
}
