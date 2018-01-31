package com.omnia.admin.controller;

import com.omnia.admin.service.BalanceService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("balance")
public class BalanceController {
    private final BalanceService balanceService;

    @GetMapping("{year}/{month}")
    public ResponseEntity getMonthBalance(@PathVariable int year, @PathVariable String month,
                                          @RequestParam(required = false) List<Long> advertiserIds) {
        return ResponseEntity.ok(balanceService.getMonthBalance(year, month, advertiserIds));
    }

    @GetMapping("advertiser")
    public ResponseEntity getAdvertiserBalance(@RequestParam String name) {
        return ResponseEntity.ok().build();
    }
}
