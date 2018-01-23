package com.omnia.admin.controller;

import com.google.common.collect.Lists;
import com.omnia.admin.model.Buyer;
import com.omnia.admin.model.Role;
import com.omnia.admin.service.BuyerService;
import com.omnia.admin.service.SpentService;
import com.omnia.admin.util.UserPrincipalUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity getSpentReport(HttpServletRequest request,
                                         @RequestParam(required = false) List<Integer> buyerIds,
                                         @RequestParam(required = false) List<String> sources,
                                         @RequestParam String from,
                                         @RequestParam String to) {
        if (UserPrincipalUtils.isRole(request, Role.BUYER)) {
            int buyerId = UserPrincipalUtils.getBuyerId(request);
            return ResponseEntity.ok(spentService.getSpentReport(Lists.newArrayList(buyerId), sources, from, to));
        }
        return ResponseEntity.ok(spentService.getSpentReport(buyerIds, sources, from, to));
    }
}
