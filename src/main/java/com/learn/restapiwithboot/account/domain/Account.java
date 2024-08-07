package com.learn.restapiwithboot.account.domain;

import com.learn.restapiwithboot.account.domain.enums.AccountRole;
import com.learn.restapiwithboot.account.domain.enums.Gender;
import com.learn.restapiwithboot.core.domain.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
@ToString
public class Account extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Comment("Account PK")
    private Long id;

    @Column(unique = true)
    @Comment("이메일")
    private String email;

    @Comment("비밀번호")
    private String password;

    @Comment("성별")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Comment("전화번호")
    private String phoneNumber;

    @Comment("권한")
    @Enumerated(EnumType.STRING)
    private AccountRole role;

    @Comment("탈퇴 여부")
    @Column(nullable = false)
    private boolean isWithdraw;

    @Builder
    public Account(String email, String password, Gender gender, String phoneNumber, AccountRole role) {
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.phoneNumber = phoneNumber.replace("-", "");
        this.role = role;
    }

    public void withDraw() {
        this.isWithdraw = true;
        this.password = null;
        this.gender = null;
        this.phoneNumber = null;
    }

}
