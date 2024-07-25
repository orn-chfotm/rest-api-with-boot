package com.learn.restapiwithboot.account.controller;

import com.learn.restapiwithboot.account.dto.request.AccountRequest;
import com.learn.restapiwithboot.account.service.AccountService;
import com.learn.restapiwithboot.core.dto.response.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody @Valid AccountRequest accountRequest) {
        return SuccessResponse.of(accountService.createAccount(accountRequest));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAccount(@AuthenticationPrincipal String accountId) {
        return SuccessResponse.of(accountService.deleteAccount(Long.parseLong(accountId)));
    }

    @GetMapping
    public ResponseEntity<?> getAccountInfo(@AuthenticationPrincipal String accountId) {
        return SuccessResponse.of(accountService.getAccountInfo(Long.parseLong(accountId)));
    }

}
