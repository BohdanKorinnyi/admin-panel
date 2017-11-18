package com.omnia.admin.controller;

import com.omnia.admin.model.BuyerKpi;
import com.omnia.admin.service.BuyerKpiService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("buyer/kpi")
public class BuyerKpiController {
    private final BuyerKpiService buyerKpiService;

    @PostMapping
    public ResponseEntity save(@RequestBody BuyerKpi buyerKpi) {
        return ResponseEntity.ok().build();
    }
}
