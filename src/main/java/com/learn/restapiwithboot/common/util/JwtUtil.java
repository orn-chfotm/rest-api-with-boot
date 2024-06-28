package com.learn.restapiwithboot.common.util;

import com.learn.restapiwithboot.account.domain.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@Component
public class JwtUtil {

    private final int accessTokenExpiration;

    private final int refreshTokenExpiration;

    private final Key secretKey;

    public JwtUtil(@Value("${jwt.secret}") String secretKey,
                            @Value("${jwt.access-exp-time}") int accessTokenExpiration,
                            @Value("${jwt.refresh-exp-time}") int refreshTokenExpiration) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public String generateAccessToken(Account account) {
        Claims claims = setClaims("accessToken", accessTokenExpiration);
        claims.put("email", account.getEmail());
        claims.put("role", account.getRole());

        return Jwts.builder()
                .setHeader(Map.of("type", "JWT", "alg", "HS256"))
                .setClaims(claims)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Account account) {
        Claims claims = setClaims("refreshToken", refreshTokenExpiration);
        claims.put("email", account.getEmail());
        claims.put("role", account.getRole());

        return Jwts.builder()
                .setHeader(Map.of("type", "JWT", "alg", "HS256"))
                .setClaims(claims)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /* Token Validation */
    public boolean validateToken(String token) {
        return !Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody().isEmpty();
    }

    /* Get Token Claims */
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Claims setClaims(String subject, int expiration) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = now.plusMinutes(expiration);


        return Jwts.claims()
                .setSubject(subject)
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expirationTime.atZone(ZoneId.systemDefault()).toInstant()))
        ;
    }


}
