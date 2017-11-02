package com.omnia.admin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("affiliates")
public class AffiliatesController {

    @GetMapping
    public List<Long> getAffiliatesIdsByBuyerId(@RequestParam("buyer_id") Long buyerId) {
        return Collections.emptyList();
    }
}
