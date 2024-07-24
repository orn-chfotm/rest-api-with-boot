package com.learn.restapiwithboot.account.controller;

import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.dto.request.AccountRequest;
import com.learn.restapiwithboot.account.service.AccountService;
import com.learn.restapiwithboot.core.annotation.CurrentUser;
import com.learn.restapiwithboot.core.dto.response.SuccessResponse;
import com.sun.security.auth.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

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
    public ResponseEntity<?> deleteAccount(@CurrentUser String accountId) {
        return SuccessResponse.of(accountService.deleteAccount(Long.parseLong(accountId)));
    }

    @GetMapping
    public ResponseEntity<?> getAccountInfo(@CurrentUser String accountId) {
        return SuccessResponse.of(accountService.getAccountInfo(Long.parseLong(accountId)));
    }

}
