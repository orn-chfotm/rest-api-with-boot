package com.learn.restapiwithboot.account.dto.response;

import com.learn.restapiwithboot.account.domain.enums.AccountRole;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter @NoArgsConstructor
public class AccountResponse {

    private String email;

    private String password;

    private Set<AccountRole> role;
}
