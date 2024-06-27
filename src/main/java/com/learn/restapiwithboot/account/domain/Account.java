package com.learn.restapiwithboot.account.domain;

import com.learn.restapiwithboot.account.domain.enums.AccountRole;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import java.util.Set;

@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
public class Account {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<AccountRole> role;

    @Builder
    public Account(String email, String password, Set<AccountRole> role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

}
