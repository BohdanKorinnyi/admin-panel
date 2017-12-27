package com.omnia.admin.controller;

import com.omnia.admin.service.AffiliatesService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("affiliates")
@AllArgsConstructor
public class AffiliatesController {

    private final AffiliatesService affiliatesService;

    @GetMapping("get")
    public ResponseEntity getAffiliates() {
        return ResponseEntity.ok(affiliatesService.findAffiliates());
    }

    @GetMapping
    public List<Long> getAffiliatesIdsByBuyerId(@RequestParam("buyer_id") Long buyerId) {
        return affiliatesService.getAffiliatesIdsByBuyerId(buyerId);
    }

    @PostMapping
    public ResponseEntity<List<Long>> create(@RequestParam("quantity") Integer quantity,
                                             @RequestParam("buyer_id") Integer buyerId) {
        return ResponseEntity.ok(affiliatesService.generate(quantity, buyerId));
    }
}
