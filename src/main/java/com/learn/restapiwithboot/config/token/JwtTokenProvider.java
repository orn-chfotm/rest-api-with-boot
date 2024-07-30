package com.learn.restapiwithboot.config.token;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.domain.enums.AccountRole;
import com.learn.restapiwithboot.config.properties.JwtProperties;
import com.learn.restapiwithboot.config.token.JwtAuthenticationToken;
import com.learn.restapiwithboot.core.exceptions.enums.ExceptionType;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
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

    private static final Map<String, Object> CLAIMS_HEADER = Map.of("type", "JWT", "alg", "HS256");

    /**
     * Reqeust Header Authentication JwtToken extract
     */
    public String extractToken(HttpServletRequest request) {
        String bearer = request.getHeader(this.jwtproperties.getHeader());
        if (bearer != null && bearer.startsWith(this.jwtproperties.getPrefix())) {
            return bearer.substring(this.jwtproperties.getPrefix().length());
        }
        return null;
    }

    public String generateToken(Account account, JwtProperties.TonkenProperties tokenProperties) {
        return Jwts.builder()
                .setHeader(CLAIMS_HEADER)
                .setClaims(generateClaims(account, tokenProperties.getExpTime()))
                .signWith(tokenProperties.getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateAccessToken(Account account) {
        return generateToken(account, this.jwtproperties.getAccessToken());
    }

    public String generateRefreshToken(Account account) {
        return generateToken(account, this.jwtproperties.getRefreshToken());
    }

    /**
     * Token Validation
     */
    public boolean validateToken(String token, Key tokenKey) {
        try {
            return getClaims(token, tokenKey) != null;
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

    /**
     * Use JwtAuthenticationProvider.class
     * new JwtAtuhenticationToken
     */
    public JwtAuthenticationToken getAuthentication(String token) {
        Claims claims = getClaims(token, this.jwtproperties.getAccessToken().getSecretKey());
        return new JwtAuthenticationToken(claims.get("accountId").toString(), token, this.getAuthorities(claims));
    }

    /* Get Token Claims */
    public Claims getClaims(String token, Key tokenKey) {
        return Jwts.parserBuilder()
                .setSigningKey(tokenKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Generate Claims
     */
    private Claims generateClaims(Account account, int expTime) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = now.plusMinutes(expTime);

        Claims claims = Jwts.claims()
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expirationTime.atZone(ZoneId.systemDefault()).toInstant()));

        claims.put("accountId", account.getId());
        claims.put("email", account.getEmail());
        claims.put("role", account.getRole());

        return claims;
    }

    /**
     * Get Conversion Authorities
     */
    private Collection<? extends GrantedAuthority> getAuthorities(Claims claims) {
        String role = (String) claims.get("role");

        if (role == null) {
            log.warn("Claims Role is Null :: {}", claims.get("email"));
            throw ExceptionType.BAD_CREDENTIALS_EXCEPTION.getException();
        }

        Set<String> roles = new HashSet<>();
        try {
             roles = AccountRole.valueOf(role).getValue();
        } catch (IllegalArgumentException e) {
            log.warn("IllegalArgumentException :: {}", e.getMessage());
            throw ExceptionType.BAD_CREDENTIALS_EXCEPTION.getException();
        }
        return roles.stream()
                .map(enumRoles -> new SimpleGrantedAuthority("ROLE_" + enumRoles))
                .collect(Collectors.toSet());
    }

}
