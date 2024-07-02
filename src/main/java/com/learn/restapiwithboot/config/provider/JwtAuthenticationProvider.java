package com.learn.restapiwithboot.config.provider;

import com.learn.restapiwithboot.account.domain.enums.AccountRole;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationProvider(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String jwtToken = ((JwtAuthenticationToken) authentication).getJwtToken();
        Claims claims = jwtTokenProvider.getClaims(jwtToken);

        return new JwtAuthenticationToken(jwtToken, claims.get("email").toString(),  authorities(claims));
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
