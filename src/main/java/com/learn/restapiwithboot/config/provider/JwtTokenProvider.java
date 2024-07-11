package com.learn.restapiwithboot.config.provider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.domain.enums.AccountRole;
import com.learn.restapiwithboot.config.properties.JwtProperties;
import com.learn.restapiwithboot.config.token.JwtAuthenticationToken;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtproperties;

    private final ObjectMapper objectMapper;

    public String extractToken(HttpServletRequest request) {
        String bearer = request.getHeader(this.jwtproperties.getHeader());
        if (bearer != null && bearer.startsWith(this.jwtproperties.getPrefix())) {
            return bearer.substring(this.jwtproperties.getPrefix().length());
        }
        return null;
    }

    public String generateAsseccToken(Account account) {
        return Jwts.builder()
                .setHeader(Map.of("type", "JWT", "alg", "HS256"))
                .setClaims(getClaims(account, this.jwtproperties.getAccessExpTime()))
                .signWith(this.jwtproperties.getAccessSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Account account) {
        return Jwts.builder()
                .setHeader(Map.of("type", "JWT", "alg", "HS256"))
                .setClaims(getClaims(account, this.jwtproperties.getRefreshExpTime()))
                .signWith(this.jwtproperties.getRefreshSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims getClaims(Account account, int expTime) {
        Claims claims = setClaims(expTime);

        claims.put("email", account.getEmail());
        claims.put("role", account.getRoles());
        return claims;
    }

    /* Token Validation */
    public boolean validateToken(String token, Key tokenKey) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(tokenKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("ExpiredJwtException :: {}", e.getMessage());
            return false;
        } catch (InvalidClaimException e) {
            log.warn("InvalidClaimException :: {}", e.getMessage());
            return false;
        } catch (JwtException e) {
            log.warn("Token Exception :: {}", e.getMessage());
            return false;
        }
    }

    /* Get Token Claims */
    public Claims getClaims(String token, Key tokenKey) {
        return Jwts.parserBuilder()
                .setSigningKey(tokenKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public JwtAuthenticationToken getAuthentication(String token) {
        Claims claims = getClaims(token, this.jwtproperties.getAccessSecretKey());
        return new JwtAuthenticationToken(claims.getSubject(), token, this.getAuthorities(claims));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Claims claims) {
        Object rolesObject = claims.get("role");
        if (rolesObject instanceof String) {
            rolesObject = List.of(rolesObject);
        }

        try {
            List<String> roles = objectMapper.convertValue(rolesObject, new TypeReference<List<String>>() {});
            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toSet());
        } catch (IllegalArgumentException e) {
            return Collections.emptySet();
        }
    }

    private Claims setClaims(int expiration) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = now.plusMinutes(expiration);

        return Jwts.claims()
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expirationTime.atZone(ZoneId.systemDefault()).toInstant()))
        ;
    }

}
