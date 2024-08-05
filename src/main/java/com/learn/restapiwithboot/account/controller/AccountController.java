package com.learn.restapiwithboot.account.controller;

import com.learn.restapiwithboot.account.dto.request.AccountRequest;
import com.learn.restapiwithboot.account.dto.request.AccountUpdateReqeust;
import com.learn.restapiwithboot.account.service.AccountService;
import com.learn.restapiwithboot.core.dto.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody @Valid AccountRequest accountRequest) {
        return SuccessResponse.of(accountService.createAccount(accountRequest));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAccount(@AuthenticationPrincipal String accountId) {
        accountService.deleteAccount(Long.parseLong(accountId));
        return SuccessResponse.of(null);
    }

    @GetMapping
    public ResponseEntity<?> getAccountInfo(@AuthenticationPrincipal String accountId) {
        return SuccessResponse.of(accountService.getAccountInfo(Long.parseLong(accountId)));
    }

    @PutMapping
    public ResponseEntity<?> updateAccount(@AuthenticationPrincipal String accountId,
                                           @RequestBody @Valid AccountUpdateReqeust accountUpdateReqeust) {
        return SuccessResponse.of(accountService.updateAccount(Long.parseLong(accountId), accountUpdateReqeust));
    }

}
