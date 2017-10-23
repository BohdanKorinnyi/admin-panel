package com.omnia.admin.controller;

import com.omnia.admin.service.BuyerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "buyer")
public class BuyerController {
    private final BuyerService buyerService;

    @GetMapping("names")
    public List<String> getBuyersName() {
        return buyerService.getBuyersName();
    }
}
