package com.omnia.admin.controller;

import com.omnia.admin.service.BuyerPlanService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("buyer")
@AllArgsConstructor
public class BuyerPlanController {
    private final BuyerPlanService buyerPlanService;

    @GetMapping("plan")
    public ResponseEntity getPlan() {
        return ResponseEntity.ok(buyerPlanService.getBuyerPlan());
    }
}