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
@Setter @Getter
public class JwtProperties {

    @PostConstruct
    public void init() {
        this.accessSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.AccessSecret));
        this.refreshSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.RefreshSecret));
    }

    private Key accessSecretKey;

    private Key refreshSecretKey;

    @NotEmpty
    private String header;

    @NotEmpty
    private String prefix;

    @NotEmpty
    private int accessExpTime;

    @NotEmpty
    private int refreshExpTime;

    @NotEmpty
    @Getter(AccessLevel.NONE)
    private String AccessSecret;

    @NotEmpty
    @Getter(AccessLevel.NONE)
    private String RefreshSecret;
}
