package com.omnia.admin.controller;

import com.omnia.admin.model.Buyer;
import com.omnia.admin.service.BuyerService;
import com.omnia.admin.service.SpentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "buyer")
public class BuyerController {
    private final BuyerService buyerService;
    private final SpentService spentService;

    @GetMapping
    public List<Buyer> getBuyers() {
        return buyerService.getBuyers();
    }

    @GetMapping("names")
    public List<String> getBuyersName() {
        return buyerService.getBuyersName();
    }

    @PutMapping("update")
    public void updateBuyers(@RequestBody List<Buyer> buyers) {
        buyerService.updateBuyers(buyers);
    }

    @PostMapping("save")
    public void saveBuyers(@RequestBody List<Buyer> buyers) {
        buyerService.saveBuyers(buyers);
    }

    @GetMapping("spent/report")
    public ResponseEntity getSpentReport(@RequestParam String from, @RequestParam String to) {
        return ResponseEntity.ok(spentService.getSpentReport(from, to));
    }
}
