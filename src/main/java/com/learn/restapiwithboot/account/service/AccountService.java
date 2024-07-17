package com.learn.restapiwithboot.account.service;

import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.dto.response.AccountResponse;
import com.learn.restapiwithboot.account.repository.AccountRepository;
import com.learn.restapiwithboot.account.mapper.AccountMapper;
import com.learn.restapiwithboot.core.exceptions.AccountExistenceException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    private final AccountMapper accountMapper;

    @Transactional
    public AccountResponse createAccount(Account account) {
        Optional<Account> getAccount = accountRepository.findByEmail(account.getEmail());

        if (getAccount.isPresent()) {
            if (getAccount.get().isWithdraw()) {
               throw new AccountExistenceException("탈퇴한 계정입니다.");
            } else {
                throw new AccountExistenceException("이미 존재하는 계정입니다.");
            }
        }

        account.setPassword(this.passwordEncoder.encode(account.getPassword()));
        Account saveAccount = accountRepository.save(account);

        return accountMapper.accountToAccountResponse(saveAccount);
    }

    @Transactional
    public AccountResponse reJoinAccount(Account account) {
        Account getAccount = accountRepository.findByEmail(account.getEmail())
                .orElseThrow(() -> new AccountExistenceException("존재하지 않는 계정입니다."));

        if (getAccount.isWithdraw()) {
            getAccount.setPassword(this.passwordEncoder.encode(account.getPassword()));
            getAccount.setGender(account.getGender());
            getAccount.setPhoneNumber(account.getPhoneNumber().replaceAll("-", ""));
            getAccount.withDraw();

            return accountMapper.accountToAccountResponse(getAccount);
        } else {
            throw new AccountExistenceException("탈퇴하지 않은 계정입니다.");
        }
    }

    @Transactional
    public boolean deleteAccount(Long accountId) {
        Account getAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountExistenceException("존재하지 않는 계정입니다."));

        if (!getAccount.isWithdraw()) {
            getAccount.withDraw();
        } else {
            throw new AccountExistenceException("탈퇴한 계정입니다.");
        }

        return true;
    }
}
