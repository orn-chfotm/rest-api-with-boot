package com.learn.restapiwithboot.config.authentication;

import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.repository.AccountRepository;
import com.learn.restapiwithboot.config.token.CustomUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("CustomUserDetailService loadUserByUsername :: {}", email);
        return new CustomUser(getAccount(email));
    }

    private Account getAccount(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
