package com.learn.restapiwithboot.account.dto.response;

import com.learn.restapiwithboot.account.domain.enums.AccountRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class AccountResponse {

    private String email;

    private Set<AccountRole> roles;

    private String gender;

    private String phoneNumber;
}
