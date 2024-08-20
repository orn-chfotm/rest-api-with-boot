package com.learn.restapiwithboot.account.dto.response;

import com.learn.restapiwithboot.account.domain.enums.AccountRole;
import lombok.*;

import java.util.Set;

@Getter
@NoArgsConstructor
public class AccountResponse {

    private String email;
    private String gender;
    private String phoneNumber;
    private AccountRole role;

    @Builder
    public AccountResponse(String email, String gender, String phoneNumber, AccountRole role) {
        this.email = email;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }
}
