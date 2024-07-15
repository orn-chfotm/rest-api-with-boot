package com.learn.restapiwithboot.account.domain;

import com.learn.restapiwithboot.account.domain.enums.AccountRole;
import com.learn.restapiwithboot.core.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
@ToString
public class Account extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<AccountRole> roles;

    @Builder
    public Account(String email, String password, Set<AccountRole> roles) {
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

}
