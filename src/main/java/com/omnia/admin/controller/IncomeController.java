package com.omnia.admin.controller;

import com.omnia.admin.model.Income;
import com.omnia.admin.service.IncomeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("income")
public class IncomeController {
    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity save(@RequestBody Income income) {
        incomeService.save(income);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
