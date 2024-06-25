package com.learn.restapiwithboot.account.controller;

import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.service.AccountService;
import com.learn.restapiwithboot.common.dto.response.SuccessResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody Account account) {
        ModelMapper modelMapper = new ModelMapper();
        return SuccessResponse.of(accountService.createAccount(account));
    }

}
