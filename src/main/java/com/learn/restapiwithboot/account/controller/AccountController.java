package com.learn.restapiwithboot.account.controller;

import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.service.AccountService;
import com.learn.restapiwithboot.core.dto.response.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody Account account) {
        return SuccessResponse.of(accountService.createAccount(account));
    }

}
