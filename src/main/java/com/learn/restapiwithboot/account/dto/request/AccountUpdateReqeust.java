package com.learn.restapiwithboot.account.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class AccountUpdateReqeust {

    @NotNull(message = "성별을 입력해주세요.")
    private String gender;

    @NotNull(message = "전화번호를 입력해주세요.")
    private String phoneNumber;

    @Builder
    public AccountUpdateReqeust(String gender, String phoneNumber) {
        this.gender = gender;
        this.phoneNumber = phoneNumber;
    }
}
