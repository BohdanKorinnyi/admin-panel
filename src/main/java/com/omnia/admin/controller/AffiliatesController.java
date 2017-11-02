package com.omnia.admin.controller;

import com.omnia.admin.service.AffiliatesService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
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
}
