package com.omnia.admin.controller;

import com.omnia.admin.service.CurrencyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("currency")
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping
    public ResponseEntity getAllCurrency() {
        return ResponseEntity.ok(currencyService.getCurrencies());
    }
}
