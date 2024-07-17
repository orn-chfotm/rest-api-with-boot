package com.learn.restapiwithboot.account.controller;

import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.service.AccountService;
import com.learn.restapiwithboot.core.dto.response.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

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

    @PostMapping("/rejoin")
    public ResponseEntity<?> rejoinAccount(@RequestBody Account account) {
        return SuccessResponse.of(accountService.reJoinAccount(account));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAccount(Principal principal) {
        return SuccessResponse.of(accountService.deleteAccount(Long.parseLong(principal.getName())));
    }

}
