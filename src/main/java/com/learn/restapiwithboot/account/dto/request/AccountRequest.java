package com.learn.restapiwithboot.account.dto.request;

import com.learn.restapiwithboot.account.domain.enums.AccountRole;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@NoArgsConstructor
public class AccountRequest {

    private String email;

    private String password;

    private Set<AccountRole> roles;

    @Builder
    public AccountRequest(String email, String password, Set<AccountRole> roles) {
        this.email = email;
        this.password = password;
        this.roles = roles;
    }
}
