package com.omnia.admin.controller;

import com.omnia.admin.dto.PageResponse;
import com.omnia.admin.grid.Page;
import com.omnia.admin.model.Payroll;
import com.omnia.admin.model.PayrollType;
import com.omnia.admin.model.Role;
import com.omnia.admin.service.PayrollService;
import com.omnia.admin.util.UserPrincipalUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "payroll")
public class PayrollController {

    private final PayrollService payrollService;

    @GetMapping("bonus/year/{year}")
    public ResponseEntity getPayrollByBuyerAndYear(HttpServletRequest request, @PathVariable int year) {
        return ResponseEntity.ok(payrollService.getPayrollsByBuyerAndYear(UserPrincipalUtils.getBuyerId(request), year));
    }

    @PostMapping
    public ResponseEntity getPayrolls(HttpServletRequest request, @RequestBody Page page) {
        Integer total;
        List<Payroll> payrolls;
        if (UserPrincipalUtils.isRole(request, Role.BUYER)) {
            int buyerId = UserPrincipalUtils.getBuyerId(request);
            total = payrollService.countAll(buyerId);
            payrolls = payrollService.findPayrolls(page, buyerId);
        } else {
            total = payrollService.countAll(null);
            payrolls = payrollService.findPayrolls(page, null);
        }
        return ResponseEntity.ok(new PageResponse(total, page.getNumber(), payrolls));
    }

    @GetMapping("staff/{id}")
    public ResponseEntity getPayrollByStaff(@PathVariable int id) {
        return ResponseEntity.ok(payrollService.getPayrollsByStaffId(id));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void update(@RequestBody Payroll payrolls) {
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

    @GetMapping("description")
    public List<String> getPayrollDescription() {
        return payrollService.getPayrollDescription();
    }

    @GetMapping("types")
    public List<PayrollType> getTypes() {
        return payrollService.getTypes();
    }
}
