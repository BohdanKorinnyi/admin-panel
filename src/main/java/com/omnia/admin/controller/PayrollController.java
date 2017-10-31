package com.omnia.admin.controller;

import com.omnia.admin.dto.PageResponse;
import com.omnia.admin.grid.Page;
import com.omnia.admin.model.Payroll;
import com.omnia.admin.service.PayrollService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "payroll")
public class PayrollController {

    private final PayrollService payrollService;

    @PostMapping
    public ResponseEntity getPayrolls(@RequestBody Page page) {
        Integer total = payrollService.countAll();
        List<Payroll> payrolls = payrollService.findPayrolls(page);
        return ResponseEntity.ok(new PageResponse(total, page.getNumber(), payrolls));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void update(@RequestBody List<Payroll> payrolls) {
        payrollService.update(payrolls);
    }

    @PostMapping(path = "save")
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody List<Payroll> payrolls) {
        payrollService.save(payrolls);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestBody List<Long> ids) {
        payrollService.delete(ids);
    }
}
