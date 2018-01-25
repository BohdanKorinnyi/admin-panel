package com.omnia.admin.controller;

import com.omnia.admin.service.PaymentService;
import com.omnia.admin.util.UserPrincipalUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping("payment")
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/year/{year}")
    public ResponseEntity getPaymentByBuyerAndYear(HttpServletRequest request, @PathVariable int year) {
        return ResponseEntity.ok(paymentService.getPaymentByBuyerAndYear(UserPrincipalUtils.getBuyerId(request), year));
    }
}
