package com.omnia.admin.service;

import com.omnia.admin.grid.Page;
import com.omnia.admin.model.Payroll;

import java.util.List;

public interface PayrollService {
    Integer countAll();

    List<Payroll> findPayrolls(Page page);

    List<Payroll> findPayrollsByBuyerId(Integer buyerId);

    void update(Payroll payroll);

    void save(List<Payroll> payrolls);

    void delete(List<Long> ids);

    List<String> getPayrollDescription();
}
