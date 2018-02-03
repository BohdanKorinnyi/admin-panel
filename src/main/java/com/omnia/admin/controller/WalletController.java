package com.omnia.admin.controller;

import com.omnia.admin.dao.WalletDao;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class WalletController {
    private final WalletDao walletDao;

    @GetMapping("wallet")
    public ResponseEntity getWallets() {
        return ResponseEntity.ok(walletDao.findAll());
    }
}
