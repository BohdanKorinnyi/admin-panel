package com.omnia.admin.controller;

import com.omnia.admin.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("account")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("types")
    public ResponseEntity getAccountTypes() {
        return ResponseEntity.ok(accountService.getAccountTypes());
    }

    @GetMapping("finance")
    public ResponseEntity getFinanceAccounts() {
        return ResponseEntity.ok(accountService.getFinanceAccounts());
    }
}
