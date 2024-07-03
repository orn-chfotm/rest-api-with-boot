package com.learn.restapiwithboot.config.provider;

import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.domain.enums.AccountRole;
import com.learn.restapiwithboot.config.provider.properties.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Set;

@Component
public class JwtTokenProvider {

    private final JwtProperties jwtproperties;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtproperties = jwtProperties;
    }

    public String generateToken(Account account, Key tokenKey, int expTime) {
        Claims claims = setClaims(expTime);
        if(tokenKey.equals(this.jwtproperties.getAccessSecretKey())) {
            claims.setSubject("accessToken");
        }
        if(tokenKey.equals(this.jwtproperties.getRefreshSecretKey())) {
            claims.setSubject("refreshToken");
        }
        Set<AccountRole> roles = account.getRoles();
        claims.put("email", account.getEmail());
        claims.put("role", roles);

        return Jwts.builder()
                .setHeader(Map.of("type", "JWT", "alg", "HS256"))
                .setClaims(claims)
                .signWith(tokenKey, SignatureAlgorithm.HS256)
                .compact();
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
        } catch (JwtException e) {
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
