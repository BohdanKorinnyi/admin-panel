package com.omnia.admin.controller;

import com.omnia.admin.grid.Page;
import com.omnia.admin.model.Expenses;
import com.omnia.admin.model.Role;
import com.omnia.admin.service.ExpensesService;
import com.omnia.admin.util.UserPrincipalUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("expenses")
public class ExpensesController {
    private final ExpensesService expensesService;

    @PostMapping
    public ResponseEntity getExpenses(HttpServletRequest request,
                                      @RequestBody Page page,
                                      @RequestParam(required = false) List<Integer> buyerIds,
                                      @RequestParam(required = false) String from,
                                      @RequestParam(required = false) String to) {
        if (UserPrincipalUtils.isRole(request, Role.BUYER)) {
            int buyerId = UserPrincipalUtils.getBuyerId(request);
            return ResponseEntity.ok(expensesService.getExpenses(page, Collections.singletonList(buyerId), from, to));
        }
        return ResponseEntity.ok(expensesService.getExpenses(page, buyerIds, from, to));
    }

    @PutMapping
    public void updateExpenses(HttpServletRequest request, @RequestBody Expenses expenses) {
        expensesService.updateExpenses(expenses);
    }
}
