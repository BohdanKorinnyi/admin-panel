package com.omnia.admin.controller;

import com.omnia.admin.model.ExpensesType;
import com.omnia.admin.service.ExpensesTypeService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j
@RestController
@AllArgsConstructor
@RequestMapping("expenses/type")
public class ExpensesTypeController {
    private final ExpensesTypeService expensesTypeService;

    @GetMapping("all")
    public ResponseEntity getAllExpenses() {
        return ResponseEntity.ok(expensesTypeService.findAll());
    }

    @PostMapping("save")
    public void saveExpenses(@RequestParam String name) {
        expensesTypeService.save(name);
    }

    @PutMapping("update")
    public void updateExpenses(@RequestBody ExpensesType expensesType) {
        expensesTypeService.update(expensesType);
    }
}
