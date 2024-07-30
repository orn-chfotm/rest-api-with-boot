package com.learn.restapiwithboot.config.authentication;

import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.domain.enums.AccountRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

import java.util.stream.Collectors;

@Getter
public class CustomUser extends User {

    private final Account account;

    public CustomUser(Account account) {
        super(account.getEmail(), account.getPassword(), authorities(account.getRole()));
        this.account = account;
    }

    private static Collection<? extends GrantedAuthority> authorities(AccountRole roles) {
        return roles.getValue().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
        //return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
}
