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

    /**
     * Principal 객체의 name 값이기에 무조건 값이 존재하지만
     * 존재하지 않는 계정을 삭제하려고 할 경우를 대비하여 existsById 메소드로 존재 여부를 확인
     */
    @Transactional
    public boolean deleteAccount(Long accountId) {
        boolean isexists = accountRepository.existsById(accountId);
        if (isexists) {
            accountRepository.deleteById(accountId);
        }
        return isexists;
    }
}
