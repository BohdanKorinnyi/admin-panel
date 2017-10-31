package com.omnia.admin.service;

import com.omnia.admin.grid.Page;
import com.omnia.admin.model.Payroll;

import java.util.List;

public interface PayrollService {
    List<Payroll> findPayrolls(Page page);

    void update(List<Payroll> payrolls);

    void save(List<Payroll> payrolls);

    void delete(List<Long> ids);
}
