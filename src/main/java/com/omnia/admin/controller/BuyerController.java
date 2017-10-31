package com.omnia.admin.controller;

import com.omnia.admin.model.Buyer;
import com.omnia.admin.service.BuyerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "buyer")
@Api(description = "Operations about buyer")
public class BuyerController {
    private final BuyerService buyerService;

    @GetMapping
    public List<Buyer> getBuyers() {
        return buyerService.getBuyers();
    }

    @GetMapping("names")
    @ApiOperation(value = "Returns list of all buyers")
    public List<String> getBuyersName() {
        return buyerService.getBuyersName();
    }
}
