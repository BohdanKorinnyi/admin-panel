package com.omnia.admin.controller;

import com.omnia.admin.model.Role;
import com.omnia.admin.service.FinanceTotalService;
import com.omnia.admin.util.UserPrincipalUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping("finance/total")
public class FinanceTotalController {
    private final FinanceTotalService financeTotalService;

    @GetMapping
    public ResponseEntity getTotalFinance(HttpServletRequest request, @RequestParam String from, @RequestParam String to) {
        if (UserPrincipalUtils.isRole(request, Role.BUYER)) {
            return ResponseEntity.ok(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(financeTotalService.getFinanceTotal(from, to));
    }
}