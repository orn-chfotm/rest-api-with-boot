package com.learn.restapiwithboot.account.service;

import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.domain.enums.AccountRole;
import com.learn.restapiwithboot.account.dto.request.AccountRequest;
import com.learn.restapiwithboot.account.dto.request.AccountUpdateReqeust;
import com.learn.restapiwithboot.account.dto.response.AccountResponse;
import com.learn.restapiwithboot.account.mapper.AccountMapper;
import com.learn.restapiwithboot.account.repository.AccountRepository;
import com.learn.restapiwithboot.core.exceptions.exception.BaseException;
import com.learn.restapiwithboot.core.exceptions.enums.impl.AccountErrorType;
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
    public AccountResponse createAccount(AccountRequest accountRequest) {
        Optional<Account> getAccount = accountRepository.findByEmail(accountRequest.getEmail());

        if (getAccount.isPresent()) {
            if (getAccount.get().isWithdraw()) {
                throw new BaseException(AccountErrorType.ACCOUNT_WITHDRAWAL);
            } else {
                throw new BaseException(AccountErrorType.ACCOUNT_EXIST);
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
                .orElseThrow(() -> new BaseException(AccountErrorType.ACCOUNT_NOT_FOUND));

        if (getAccount.isWithdraw()) {
            throw new BaseException(AccountErrorType.ACCOUNT_WITHDRAWAL);
        }

        getAccount.withDraw();
    }

    @Transactional(readOnly = true)
    public AccountResponse getAccountInfo(Long accountId) {
        Account getAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new BaseException(AccountErrorType.ACCOUNT_NOT_FOUND));

        return accountMapper.accountToAccountResponse(getAccount);
    }

    public AccountResponse updateAccount(long accountId, AccountUpdateReqeust accountUpdateReqeust) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new BaseException(AccountErrorType.ACCOUNT_NOT_FOUND));

        Account toAccount = accountMapper.accountUpdateRequestToAccount(accountUpdateReqeust, account);

        return accountMapper.accountToAccountResponse(toAccount);
    }
}
