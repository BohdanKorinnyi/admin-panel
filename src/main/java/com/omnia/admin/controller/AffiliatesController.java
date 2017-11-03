package com.omnia.admin.controller;

import com.omnia.admin.service.AffiliatesService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("affiliates")
@AllArgsConstructor
public class AffiliatesController {

    private final AffiliatesService affiliatesService;

    @GetMapping
    public List<Long> getAffiliatesIdsByBuyerId(@RequestParam("buyer_id") Long buyerId) {
        return affiliatesService.getAffiliatesIdsByBuyerId(buyerId);
    }

    @PostMapping
    public void create(@RequestParam("quantity") Integer quantity, @RequestParam("buyer_id") Integer buyerId) {
        affiliatesService.generate(quantity, buyerId);
    }
}
