package com.learn.restapiwithboot.account.dto.request;

import com.learn.restapiwithboot.account.domain.enums.AccountRole;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import java.util.Set;

@Getter
@NoArgsConstructor
public class AccountRequest {

    private String email;

    private String password;

    private Set<AccountRole> roles;

    private String gender;

    private String phoneNumber;

    @Builder
    public AccountRequest(String email, String password, Set<AccountRole> roles, String gender, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
    }
}
