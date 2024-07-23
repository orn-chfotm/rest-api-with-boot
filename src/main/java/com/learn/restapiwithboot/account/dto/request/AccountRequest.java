package com.learn.restapiwithboot.account.dto.request;

import com.learn.restapiwithboot.account.domain.enums.AccountRole;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@NoArgsConstructor
public class AccountRequest {

    @NotNull(message = "이메일을 입력해주세요.")
    private String email;

    @NotNull(message = "비밀번호를 입력해주세요.")
    private String password;

    private AccountRole roles;

    @NotNull(message = "성별을 입력해주세요.")
    private String gender;

    @NotNull(message = "전화번호를 입력해주세요.")
    private String phoneNumber;

    @Builder
    public AccountRequest(String email, String password, AccountRole roles, String gender, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
    }
}
