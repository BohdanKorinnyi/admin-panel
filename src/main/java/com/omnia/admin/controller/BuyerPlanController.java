package com.omnia.admin.controller;

import com.omnia.admin.model.Role;
import com.omnia.admin.service.BuyerPlanService;
import com.omnia.admin.util.UserPrincipalUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.omnia.admin.util.UserPrincipalUtils.getBuyerId;

@RestController
@RequestMapping("buyer")
@AllArgsConstructor
public class BuyerPlanController {
    private final BuyerPlanService buyerPlanService;

    @GetMapping("marginality")
    public ResponseEntity getMarginality(HttpServletRequest request) throws ExecutionException, InterruptedException {
        if (UserPrincipalUtils.isRole(request, Role.BUYER)) {
            return ResponseEntity.ok(buyerPlanService.getMarginality(getBuyerId(request)));
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("plan/revenue")
    public ResponseEntity getRevenuePlanByBuyer(HttpServletRequest request) {
        if (UserPrincipalUtils.isRole(request, Role.BUYER)) {
            return ResponseEntity.ok(buyerPlanService.getBuyerRevenuePlan(getBuyerId(request)));
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("plan")
    public ResponseEntity getPlan(HttpServletRequest request,
                                  @RequestParam(required = false) List<Integer> buyers,
                                  @RequestParam(required = false) List<String> month
    ) throws ExecutionException, InterruptedException {
        if (UserPrincipalUtils.isRole(request, Role.BUYER)) {
            return ResponseEntity.ok(buyerPlanService.getBuyerPlan(
                    Collections.singletonList(getBuyerId(request)), month));
        }
        return ResponseEntity.ok(buyerPlanService.getBuyerPlan(buyers, month));
    }
}