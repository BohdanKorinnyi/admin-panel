package com.omnia.admin.dao;

import com.omnia.admin.grid.Page;
import com.omnia.admin.model.Payroll;

import java.util.List;

public interface PayrollDao {
    Integer countAll();

    List<Payroll> findPayrolls(Page page);

    void update(Payroll payroll);

    void save(List<Payroll> payrolls);

    void delete(List<Long> ids);

    List<String> getPayrollDescription();
}
