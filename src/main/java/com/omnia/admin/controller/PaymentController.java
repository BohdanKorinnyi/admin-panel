package com.omnia.admin.controller;

import com.omnia.admin.model.Payment;
import com.omnia.admin.model.Role;
import com.omnia.admin.service.PaymentService;
import com.omnia.admin.util.UserPrincipalUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@RestController
@AllArgsConstructor
@RequestMapping("payment")
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/year/{year}")
    public ResponseEntity getPaymentByBuyerAndYear(HttpServletRequest request, @PathVariable int year) {
        return ResponseEntity.ok(paymentService.getByBuyerAndYear(UserPrincipalUtils.getBuyerId(request), year));
    }

    @GetMapping
    public ResponseEntity getPayments(HttpServletRequest request, @RequestParam(required = false) List<Integer> buyerIds) {
        if (UserPrincipalUtils.isRole(request, Role.BUYER)) {
            int buyerId = UserPrincipalUtils.getBuyerId(request);
            return ResponseEntity.ok(paymentService.getByBuyer(newArrayList(buyerId)));
        }
        return ResponseEntity.ok(paymentService.getByBuyer(buyerIds));
    }

    @PostMapping
    public void savePayments(@RequestBody List<Payment> payments) {
        paymentService.save(payments);
    }
}
