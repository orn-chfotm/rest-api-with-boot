package com.learn.restapiwithboot.config.provider;

import com.learn.restapiwithboot.config.authentication.JwtAuthenticationToken;
import com.learn.restapiwithboot.config.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtTokenProvider jwtTokenProvider;

    private final JwtProperties properties;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String jwtToken = ((JwtAuthenticationToken) authentication).getJwtToken();
        Claims claims = jwtTokenProvider.getClaims(jwtToken, properties.getAccessSecretKey());

        return new JwtAuthenticationToken(claims.get("email").toString(), jwtToken, authorities(claims));
    }

    private static Collection<? extends GrantedAuthority> authorities(Claims claims) {
        List<String> role = (List<String>) claims.get("role");
        return role.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
