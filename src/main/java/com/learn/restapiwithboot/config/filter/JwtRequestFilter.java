package com.learn.restapiwithboot.config.filter;

import com.learn.restapiwithboot.config.properties.JwtProperties;
import com.learn.restapiwithboot.config.token.JwtAuthenticationToken;
import com.learn.restapiwithboot.config.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;
    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProvider.extractToken(request);

        if (token != null && jwtTokenProvider.validateToken(token, jwtProperties.getAccessToken().getSecretKey())) {
            JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(token);
            jwtAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            try {
                JwtAuthenticationToken authenticate = (JwtAuthenticationToken) authenticationManager.authenticate(jwtAuthenticationToken);
                SecurityContextHolder.getContext().setAuthentication(authenticate);
            } catch (BadCredentialsException e) {
                log.error("BadCredentialsException: {}", e.getMessage());
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }
}
