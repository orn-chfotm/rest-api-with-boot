package com.learn.restapiwithboot.account.service;

import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.domain.enums.AccountRole;
import com.learn.restapiwithboot.account.dto.request.AccountRequest;
import com.learn.restapiwithboot.account.dto.response.AccountResponse;
import com.learn.restapiwithboot.account.repository.AccountRepository;
import com.learn.restapiwithboot.account.mapper.AccountMapper;
import com.learn.restapiwithboot.core.exceptions.enums.ExceptionType;
import com.learn.restapiwithboot.core.exceptions.impl.AccountExistenceException;
import com.learn.restapiwithboot.core.exceptions.impl.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    private final AccountMapper accountMapper;

    @Transactional
    public AccountResponse createAccount(AccountRequest accountRequest) {
        Optional<Account> getAccount = accountRepository.findByEmail(accountRequest.getEmail());

        if (getAccount.isPresent()) {
            if (getAccount.get().isWithdraw()) {
               throw ExceptionType.ACCOUNT_WITHDRAWAL_EXCEPTION.getException();
            } else {
                throw ExceptionType.ACCOUNT_EXIST_EXCEPTION.getException();
            }
        }

        Account account = accountMapper.accountRequestToAccount(accountRequest);
        account.setRole(AccountRole.USER);

        account.setPassword(this.passwordEncoder.encode(account.getPassword()));
        Account saveAccount = accountRepository.save(account);

        return accountMapper.accountToAccountResponse(saveAccount);
    }

    @Transactional
    public void deleteAccount(Long accountId) {
        Account getAccount = accountRepository.findById(accountId)
                .orElseThrow(ExceptionType.ACCOUNT_NOT_FOUND::getException);

        if (getAccount.isWithdraw()) {
            throw ExceptionType.ACCOUNT_WITHDRAWAL_EXCEPTION.getException();
        }

        getAccount.withDraw();
    }

    @Transactional(readOnly = true)
    public AccountResponse getAccountInfo(Long accountId) {
        Account getAccount = accountRepository.findById(accountId)
                .orElseThrow(ExceptionType.ACCOUNT_NOT_FOUND::getException);

        return accountMapper.accountToAccountResponse(getAccount);
    }
}
