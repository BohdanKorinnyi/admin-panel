package com.omnia.admin.controller;

import com.omnia.admin.service.BuyerPlanService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("buyer")
@AllArgsConstructor
public class BuyerPlanController {
    private final BuyerPlanService buyerPlanService;

    @GetMapping("plan")
    public ResponseEntity getPlan(@RequestParam(required = false) List<String> buyers, @RequestParam(required = false) List<String> month) {
        return ResponseEntity.ok(buyerPlanService.getBuyerPlan(buyers, month));
    }
}