package com.learn.restapiwithboot.config.properties;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotEmpty;
import java.security.Key;

@Component
@ConfigurationProperties(prefix = "jwt")
@Setter
@Getter
public class JwtProperties {

    private String header;

    private String prefix;

    private TonkenProperties accessToken;

    private TonkenProperties refreshToken;

    @Getter
    @Setter
    public static class TonkenProperties {

        private int expTime;

        private Key secretKey;

        public void setSecret(String secret) {
            this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        }
    }
}
