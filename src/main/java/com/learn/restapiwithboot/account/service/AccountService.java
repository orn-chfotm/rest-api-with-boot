package com.learn.restapiwithboot.account.service;

import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.dto.response.AccountResponse;
import com.learn.restapiwithboot.account.repository.AccountRepository;
import com.learn.restapiwithboot.account.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    private final AccountMapper accountMapper;

    @Transactional
    public AccountResponse createAccount(Account account) {
        account.setPassword(this.passwordEncoder.encode(account.getPassword()));
        Account saveAccount = accountRepository.save(account);

        return accountMapper.accountToAccountResponse(saveAccount);
    }

    @Transactional
    public boolean deleteAccount(Long id) {
        accountRepository.deleteById(id);
        return true;
    }
}
