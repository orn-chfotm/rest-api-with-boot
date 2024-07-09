package com.learn.restapiwithboot.config.provider;

import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.domain.enums.AccountRole;
import com.learn.restapiwithboot.config.properties.JwtProperties;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class JwtTokenProvider {

    private final JwtProperties jwtproperties;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtproperties = jwtProperties;
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

        Set<AccountRole> roles = account.getRoles();
        claims.put("email", account.getEmail());
        claims.put("role", roles);
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

    /*public Authentication getAuthentication(String token) {
        Claims claims = this.getClaims(token);

        if (claims.get("email") == null || claims.get("role") == null) {
            throw new IllegalArgumentException("Invalid Token");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.get("email").toString());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

    }*/

    private Claims setClaims(int expiration) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = now.plusMinutes(expiration);

        return Jwts.claims()
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expirationTime.atZone(ZoneId.systemDefault()).toInstant()))
        ;
    }
}
