package com.omnia.admin.controller;

import com.omnia.admin.model.BuyerKpi;
import com.omnia.admin.service.BuyerKpiService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("buyer/kpi")
public class BuyerKpiController {
    private final BuyerKpiService buyerKpiService;

    @PostMapping
    public ResponseEntity save(@RequestBody List<BuyerKpi> buyerKpi, @RequestParam int buyerId) {
        buyerKpiService.save(buyerKpi, buyerId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity getByBuyerId(@RequestParam int buyerId) {
        return ResponseEntity.ok(buyerKpiService.getByBuyerId(buyerId));
    }
}
