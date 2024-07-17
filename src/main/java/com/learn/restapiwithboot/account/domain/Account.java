package com.learn.restapiwithboot.account.domain;

import com.learn.restapiwithboot.account.domain.enums.AccountRole;
import com.learn.restapiwithboot.core.domain.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.util.Set;

@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
@ToString
public class Account extends BaseTimeEntity {

    @Id @GeneratedValue
    @Comment("Account PK")
    private Long id;

    @Column(unique = true)
    @Comment("이메일")
    private String email;

    @Comment("비밀번호")
    @Column(nullable = false)
    private String password;

    @Comment("성별")
    @Column(nullable = false)
    private String gender;

    @Comment("전화번호")
   @Column(nullable = false)
    private String phoneNumber;

    @Comment("권한")
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<AccountRole> roles;

    @Comment("탈퇴 여부")
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isWithdraw = false;

    @Builder
    public Account(String email, String password, String gender, String phoneNumber, Set<AccountRole> roles) {
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.phoneNumber = phoneNumber.replaceAll("-", "");
        this.roles = roles;
    }

    public void withDraw() {
        this.isWithdraw = true;
        this.password = null;
        this.gender = null;
        this.phoneNumber = null;
    }

}
