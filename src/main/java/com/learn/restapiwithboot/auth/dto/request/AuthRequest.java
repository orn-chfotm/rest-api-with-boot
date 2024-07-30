package com.learn.restapiwithboot.auth.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class AuthRequest {
    String email;

    String password;

    @Builder
    public AuthRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
