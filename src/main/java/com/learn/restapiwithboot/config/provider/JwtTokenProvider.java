package com.learn.restapiwithboot.config.provider;

import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.domain.enums.AccountRole;
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

    private final int accessTokenExpiration;

    private final int refreshTokenExpiration;

    private final Key secretAccessKey;

    private final Key secretRefreshKey;

    public JwtTokenProvider(@Value("${jwt.secret-access}") String secretAccessKey,
                            @Value("${jwt.secret-refresh}") String secretRefreshKey,
                            @Value("${jwt.access-exp-time}") int accessTokenExpiration,
                            @Value("${jwt.refresh-exp-time}") int refreshTokenExpiration) {
        this.secretAccessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretAccessKey));
        this.secretRefreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretRefreshKey));
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public String generateAccessToken(Account account) {
        Claims claims = setClaims("accessToken", accessTokenExpiration);
        Set<AccountRole> roles = account.getRoles();
        claims.put("email", account.getEmail());
        claims.put("role", roles);

        return Jwts.builder()
                .setHeader(Map.of("type", "JWT", "alg", "HS256"))
                .setClaims(claims)
                .signWith(secretAccessKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Account account) {
        Claims claims = setClaims("refreshToken", refreshTokenExpiration);
        claims.put("email", account.getEmail());
        Set<AccountRole> roles = account.getRoles();
        claims.put("role", roles);

        String refreshToken = Jwts.builder()
                .setHeader(Map.of("type", "JWT", "alg", "HS256"))
                .setClaims(claims)
                .signWith(secretRefreshKey, SignatureAlgorithm.HS256)
                .compact();

        return refreshToken;
    }

    /* Token Validation */
    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretAccessKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    /* Get Token Claims */
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretAccessKey)
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
