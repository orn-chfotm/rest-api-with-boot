package com.learn.restapiwithboot.auth.controller;

import com.learn.restapiwithboot.auth.dto.request.AuthRequest;
import com.learn.restapiwithboot.auth.service.AuthService;
import com.learn.restapiwithboot.core.dto.response.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    AuthService authService;

    public AuthController(AuthService authservice) {
        this.authService = authservice;
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> getLogout(@RequestParam("refreshToken") String refreshToken) {
        return SuccessResponse.of(authService.getRefresh(refreshToken));
    }

}
